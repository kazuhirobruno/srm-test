package com.br.srm.asset.test.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.br.srm.asset.test.domain.TaxaCambio;
import com.br.srm.asset.test.exceptions.CurrencyTaxNotFoundException;
import com.br.srm.asset.test.repository.TaxaCambioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CambioService {

  private final TaxaCambioRepository taxaCambioRepository;

  public BigDecimal converter(BigDecimal valor, String moedaOrigem, String moedaDestino) {
    if (moedaOrigem.equalsIgnoreCase(moedaDestino)) {
      return valor;
    }

    TaxaCambio taxa = taxaCambioRepository
        .findFirstByMoedaOrigemAndMoedaDestinoOrderByDataAtualizacaoDesc(moedaOrigem.toUpperCase(),
            moedaDestino.toUpperCase())
        .orElseThrow(() -> new CurrencyTaxNotFoundException(
            "Taxa de câmbio não encontrada para o par: " + moedaOrigem + " -> " + moedaDestino));

    return valor.multiply(taxa.getFatorConversao()).setScale(4, RoundingMode.HALF_UP);
  }
}