package com.br.srm.asset.test.controller;

import com.br.srm.asset.test.domain.TaxaCambio;
import com.br.srm.asset.test.dtos.CreateTaxaCambioRequestDTO;
import com.br.srm.asset.test.dtos.CreateTaxaCambioResponseDTO;
import com.br.srm.asset.test.repository.TaxaCambioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/taxas-cambio")
public class TaxaCambioController {

  private final TaxaCambioRepository taxaCambioRepository;

  @Operation(summary = "Cadastrar Nova Taxa de Câmbio", description = "Registra uma nova cotação histórica no sistema. O motor de cálculo utilizará sempre o registro mais recente para operações Cross-Currency.")
  @ApiResponse(responseCode = "201", description = "Taxa de câmbio registrada com sucesso")
  @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou violando regras de validação")
  @ApiResponse(responseCode = "500", description = "Erro interno ao tentar salvar a cotação no banco de dados")
  @PostMapping
  public ResponseEntity<Object> cadastrar(@Valid @RequestBody CreateTaxaCambioRequestDTO request) {
    try {
      TaxaCambio taxaCambio = TaxaCambio.builder()
          .moedaOrigem(request.getMoedaOrigem().toUpperCase())
          .moedaDestino(request.getMoedaDestino().toUpperCase())
          .fatorConversao(request.getFatorConversao())
          .build();

      TaxaCambio novaTaxa = taxaCambioRepository.save(taxaCambio);

      CreateTaxaCambioResponseDTO response = CreateTaxaCambioResponseDTO.builder()
          .id(novaTaxa.getId())
          .moedaOrigem(novaTaxa.getMoedaOrigem())
          .moedaDestino(novaTaxa.getMoedaDestino())
          .fatorConversao(novaTaxa.getFatorConversao())
          .build();

      return ResponseEntity.status(HttpStatus.CREATED).body(response);

    } catch (Exception ex) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("timestamp", Instant.now());
      errorBody.put("status", 500);
      errorBody.put("error", "Erro ao cadastrar taxa de câmbio");
      errorBody.put("message", "Ocorreu um erro inesperado ao salvar a cotação.");

      return ResponseEntity.status(org.springframework.http.HttpStatusCode.valueOf(500)).body(errorBody);
    }
  }
}