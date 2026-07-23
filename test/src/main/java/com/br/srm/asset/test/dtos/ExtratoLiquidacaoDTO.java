package com.br.srm.asset.test.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public interface ExtratoLiquidacaoDTO {
  Long getId();

  BigDecimal getValorOriginal();

  BigDecimal getValorLiquidado();

  String getMoedaOriginal();

  String getMoedaLiquidacao();

  String getStatus();

  Instant getUpdatedAt();

  String getCedente();
}