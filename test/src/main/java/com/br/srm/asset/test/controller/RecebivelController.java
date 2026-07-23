package com.br.srm.asset.test.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
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
  public ResponseEntity<Recebivel> cadastrar(@Valid @RequestBody CreateRecebivelRequestDTO recebivel) {
    Recebivel novoRecebivel = recebivelService.cadastrarRecebivel(recebivel);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoRecebivel);
  }

  @PatchMapping("/{id}/liquidar")
  public ResponseEntity<Void> liquidar(@PathVariable Long id, @Valid @RequestBody CreateLiquidacaoRequestDTO request) {
    recebivelService.liquidar(id, request.getMoedaPagamento(), request.getTaxaBase());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/simular")
  public ResponseEntity<SimulateRecebivelResponseDTO> simular(@Valid @RequestBody SimulateRecebivelRequestDTO request) {
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
  }
}