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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.srm.asset.test.dtos.ExtratoLiquidacaoDTO;
import com.br.srm.asset.test.repository.RecebivelRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/relatorios")
@Validated
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class RelatorioController {

  private final RecebivelRepository recebivelRepository;

  @Operation(summary = "Consultar Extrato Analítico de Liquidações", description = "Retorna uma listagem paginada e filtrada dos recebíveis do sistema.")
  @ApiResponse(responseCode = "200", description = "Extrato analítico gerado com sucesso")
  @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
  @ApiResponse(responseCode = "500", description = "Falha interna ao processar a listagem")
  @GetMapping("/extrato")
  public ResponseEntity<Object> consultarExtrato(
      @Parameter(description = "Nome do cedente para busca parcial", example = "Silva") @RequestParam(required = false) String cedente,
      @Parameter(description = "Código ISO da moeda para filtrar", example = "BRL") @RequestParam(required = false) String moeda,
      @Parameter(description = "Data inicial em formato UTC para o filtro de período", example = "2026-01-01T00:00:00Z") @RequestParam(required = false) Instant dataInicio,
      @Parameter(description = "Data final em formato UTC para o filtro de período", example = "2026-12-31T23:59:59Z") @RequestParam(required = false) Instant dataFim,
      @Parameter(description = "Número da página (inicia em 0)", example = "0") @RequestParam(defaultValue = "0") @Min(0) int page,
      @Parameter(description = "Quantidade de registros por página", example = "10") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
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