package com.br.srm.asset.test.strategy;

import java.math.BigDecimal;

public interface PrecificacaoStrategy {
  BigDecimal calcularValorPresente(BigDecimal valorFace, int prazo, BigDecimal taxaBase);
}