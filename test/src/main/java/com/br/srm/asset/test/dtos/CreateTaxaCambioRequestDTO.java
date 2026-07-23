package com.br.srm.asset.test.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaxaCambioRequestDTO {
  private String moedaOrigem;
  private String moedaDestino;
  private BigDecimal fatorConversao;
}