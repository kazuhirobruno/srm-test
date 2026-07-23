package com.br.srm.asset.test.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component("CHEQUE")
public class ChequeStrategy implements PrecificacaoStrategy {

  private static final BigDecimal SPREAD = new BigDecimal("0.025");

  @Override
  public BigDecimal calcularValorPresente(BigDecimal valorFace, int prazo, BigDecimal taxaBase) {
    BigDecimal taxaTotal = BigDecimal.ONE.add(taxaBase).add(SPREAD);
    BigDecimal divisor = taxaTotal.pow(prazo);

    return valorFace.divide(divisor, 4, RoundingMode.HALF_UP);
  }
}