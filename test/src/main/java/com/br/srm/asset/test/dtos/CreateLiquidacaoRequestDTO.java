package com.br.srm.asset.test.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLiquidacaoRequestDTO {
  private String moedaPagamento;
  private BigDecimal taxaBase;
}
