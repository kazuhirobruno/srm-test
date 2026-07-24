package com.br.srm.asset.test.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.srm.asset.test.domain.Recebivel;
import com.br.srm.asset.test.dtos.CreateLiquidacaoRequestDTO;
import com.br.srm.asset.test.dtos.CreateRecebivelRequestDTO;
import com.br.srm.asset.test.dtos.SimulateRecebivelRequestDTO;
import com.br.srm.asset.test.dtos.SimulateRecebivelResponseDTO;
import com.br.srm.asset.test.exceptions.SettledTransactionException;
import com.br.srm.asset.test.exceptions.TransactionNotFoundException;
import com.br.srm.asset.test.service.RecebivelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/recebiveis")
public class RecebivelController {

  private final RecebivelService recebivelService;

  @Operation(summary = "Cadastrar um Novo Recebível", description = "Registra um novo título (Duplicata ou Cheque) no sistema com status inicial PENDENTE.")
  @ApiResponse(responseCode = "201", description = "Recebível cadastrado com sucesso")
  @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou violando regras de validação")
  @ApiResponse(responseCode = "500", description = "Erro interno ao tentar salvar o recebível no banco de dados")
  @PostMapping("/")
  public ResponseEntity<Object> cadastrar(@Valid @RequestBody CreateRecebivelRequestDTO request) {
    try {
      Recebivel novoRecebivel = recebivelService.cadastrarRecebivel(request);
      return ResponseEntity.status(HttpStatus.CREATED).body(novoRecebivel);

    } catch (Exception ex) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("timestamp", java.time.Instant.now());
      errorBody.put("status", 500);
      errorBody.put("error", "Erro interno do servidor");
      errorBody.put("message", "Ocorreu um erro inesperado ao tentar cadastrar o recebível.");
      return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(errorBody);
    }
  }

  @Operation(summary = "Liquidar um Recebível Existente", description = "Executa a liquidação financeira de um título utilizando Lock Pessimista. Calcula o valor líquido final e aplica conversão cambial se for cross-currency.")
  @ApiResponse(responseCode = "204", description = "Recebível liquidado com sucesso (Sem corpo de retorno)")
  @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou formatos incorretos")
  @ApiResponse(responseCode = "404", description = "Recebível não encontrado para o ID informado")
  @ApiResponse(responseCode = "422", description = "Regra de negócio violada (O recebível já foi liquidado)")
  @PatchMapping("/{id}/liquidar")
  public ResponseEntity<Object> liquidar(
      @Parameter(description = "ID do recebível a ser liquidado", example = "1") @PathVariable Long id,
      @Valid @RequestBody CreateLiquidacaoRequestDTO request) {
    try {
      recebivelService.liquidar(id, request.getMoedaPagamento(), request.getTaxaBase());
      return ResponseEntity.noContent().build();
    } catch (TransactionNotFoundException ex) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("timestamp", Instant.now());
      errorBody.put("status", 404);
      errorBody.put("message", ex.getMessage());
      return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(errorBody);
    } catch (SettledTransactionException ex) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("timestamp", Instant.now());
      errorBody.put("status", 422);
      errorBody.put("message", ex.getMessage());
      return ResponseEntity.status(HttpStatusCode.valueOf(422)).body(errorBody);
    }
  }

  @Operation(summary = "Simular Precificação de Recebível", description = "Executa o cálculo matemático de desconto e câmbio em tempo real sem persistir dados ou aplicar travas.")
  @ApiResponse(responseCode = "200", description = "Simulação calculada com sucesso")
  @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou violando regras de validação")
  @ApiResponse(responseCode = "500", description = "Falha interna ou ausência de taxa cambial para o par solicitado")
  @PostMapping("/simular")
  public ResponseEntity<Object> simular(@Valid @RequestBody SimulateRecebivelRequestDTO request) {
    try {
      Recebivel recebivelSimulado = Recebivel.builder()
          .valorOriginal(request.getValorOriginal())
          .prazo(request.getPrazo())
          .tipo(request.getTipo())
          .moedaOriginal(request.getMoedaOriginal().toUpperCase())
          .build();

      BigDecimal valorLiquidoFinal = recebivelService.simularPrecificacao(
          recebivelSimulado,
          request.getMoedaPagamento(),
          request.getTaxaBase());

      SimulateRecebivelResponseDTO response = new SimulateRecebivelResponseDTO(
          request.getValorOriginal(),
          valorLiquidoFinal,
          request.getMoedaPagamento().toUpperCase());

      return ResponseEntity.ok(response);
    } catch (Exception ex) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("timestamp", java.time.Instant.now());
      errorBody.put("status", 500);
      errorBody.put("error", "Erro ao processar simulação");
      errorBody.put("message", ex.getMessage() != null ? ex.getMessage() : "Falha interna ao calcular precificação.");

      return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(errorBody);
    }
  }
}