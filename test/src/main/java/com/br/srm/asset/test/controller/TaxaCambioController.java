package com.br.srm.asset.test.controller;

import com.br.srm.asset.test.domain.TaxaCambio;
import com.br.srm.asset.test.dtos.CreateTaxaCambioRequestDTO;
import com.br.srm.asset.test.dtos.CreateTaxaCambioResponseDTO;
import com.br.srm.asset.test.repository.TaxaCambioRepository;

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