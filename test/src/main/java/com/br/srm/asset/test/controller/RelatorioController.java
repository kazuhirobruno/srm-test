package com.br.srm.asset.test.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.srm.asset.test.dtos.ExtratoLiquidacaoDTO;
import com.br.srm.asset.test.repository.RecebivelRepository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/relatorios")
@Validated
@RequiredArgsConstructor
public class RelatorioController {

  private final RecebivelRepository recebivelRepository;

  @GetMapping("/extrato")
  public ResponseEntity<Object> consultarExtrato(
      @RequestParam(required = false) String cedente,
      @RequestParam(required = false) String moeda,
      @RequestParam(required = false) Instant dataInicio,
      @RequestParam(required = false) Instant dataFim,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {

    try {
      Pageable pageable = PageRequest.of(page, size, Sort.by("updated_at").descending());

      Page<ExtratoLiquidacaoDTO> extrato = recebivelRepository.obterExtratoAnalitico(
          cedente != null && !cedente.isBlank() ? cedente : null,
          moeda != null && !moeda.isBlank() ? moeda.toUpperCase() : null,
          dataInicio,
          dataFim,
          pageable);

      return ResponseEntity.ok(extrato);

    } catch (Exception ex) {
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("timestamp", Instant.now());
      errorBody.put("status", 500);
      errorBody.put("error", "Erro ao gerar extrato analítico");
      errorBody.put("message", "Ocorreu uma falha interna ao processar a listagem paginada.");
      return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(errorBody);
    }
  }
}