package com.br.srm.asset.test.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaxaCambioResponseDTO {
  private Long id;
  private String moedaOrigem;
  private String moedaDestino;
  private BigDecimal fatorConversao;
  private Instant dataAtualizacao;
}