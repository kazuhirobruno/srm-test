package com.br.srm.asset.test.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recebiveis")
public class RecebivelController {

  private final RecebivelService recebivelService;

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

  @PatchMapping("/{id}/liquidar")
  public ResponseEntity<Object> liquidar(@PathVariable Long id,
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