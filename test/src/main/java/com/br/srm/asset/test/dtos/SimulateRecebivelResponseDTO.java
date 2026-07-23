package com.br.srm.asset.test.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulateRecebivelResponseDTO {
  private BigDecimal valorOriginal;
  private BigDecimal valorLiquidoCalculado; // Resultado após a Strategy + Câmbio
  private String moedaPagamento;
}