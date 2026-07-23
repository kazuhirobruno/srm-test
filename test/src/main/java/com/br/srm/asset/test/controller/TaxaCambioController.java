package com.br.srm.asset.test.controller;

import com.br.srm.asset.test.domain.TaxaCambio;
import com.br.srm.asset.test.dtos.CreateTaxaCambioRequestDTO;
import com.br.srm.asset.test.dtos.CreateTaxaCambioResponseDTO;
import com.br.srm.asset.test.repository.TaxaCambioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/taxas-cambio")
public class TaxaCambioController {

  private final TaxaCambioRepository taxaCambioRepository;

  @PostMapping
  public ResponseEntity<CreateTaxaCambioResponseDTO> cadastrar(@RequestBody CreateTaxaCambioRequestDTO request) {
    TaxaCambio taxaCambio = new TaxaCambio();
    taxaCambio.setMoedaOrigem(request.getMoedaOrigem().toUpperCase());
    taxaCambio.setMoedaDestino(request.getMoedaDestino().toUpperCase());
    taxaCambio.setFatorConversao(request.getFatorConversao());
    TaxaCambio novaTaxa = taxaCambioRepository.save(taxaCambio);

    CreateTaxaCambioResponseDTO response = CreateTaxaCambioResponseDTO.builder()
        .id(novaTaxa.getId())
        .moedaOrigem(novaTaxa.getMoedaOrigem())
        .moedaDestino(novaTaxa.getMoedaDestino())
        .fatorConversao(novaTaxa.getFatorConversao())
        .build();

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}