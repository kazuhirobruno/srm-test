package com.br.srm.asset.test.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;

class ChequeStrategyTest {

  private final ChequeStrategy strategy = new ChequeStrategy();

  @Test
  void deveCalcularValorPresenteComSpreadDeDoisEMeioPorCento() {
    BigDecimal valorOriginal = new BigDecimal("1000.00");
    BigDecimal taxaBase = new BigDecimal("0.01");
    int prazo = 2;

    BigDecimal resultado = strategy.calcularValorPresente(valorOriginal, prazo, taxaBase);

    BigDecimal resultadoEsperado = new BigDecimal("933.5107");

    assertEquals(resultadoEsperado, resultado.setScale(4, RoundingMode.HALF_UP));
  }
}