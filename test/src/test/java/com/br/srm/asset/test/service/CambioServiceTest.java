package com.br.srm.asset.test.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.br.srm.asset.test.domain.TaxaCambio;
import com.br.srm.asset.test.exceptions.CurrencyTaxNotFoundException;
import com.br.srm.asset.test.repository.TaxaCambioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CambioServiceTest {

  @Mock
  private TaxaCambioRepository taxaCambioRepositoryMock;

  @InjectMocks
  private CambioService cambioService;

  @Test
  void deveRetornarOMesmoValorQuandoAsMoedasForemIguais() {
    BigDecimal valorOriginal = new BigDecimal("500.00");
    BigDecimal resultado = cambioService.converter(valorOriginal, "BRL", "BRL");
    assertEquals(valorOriginal, resultado);
    verifyNoInteractions(taxaCambioRepositoryMock);
  }

  @Test
  void deveAplicarConversaoCambialComSucessoQuandoAsMoedasForemDiferentes() {
    BigDecimal valorOriginal = new BigDecimal("100.00");
    TaxaCambio taxaMock = new TaxaCambio();
    taxaMock.setFatorConversao(new BigDecimal("5.500000"));

    when(taxaCambioRepositoryMock.findFirstByMoedaOrigemAndMoedaDestinoOrderByCreatedAtDesc("USD", "BRL"))
        .thenReturn(Optional.of(taxaMock));
    BigDecimal resultado = cambioService.converter(valorOriginal, "USD", "BRL");
    BigDecimal resultadoEsperado = new BigDecimal("550.0000");
    assertEquals(resultadoEsperado, resultado);
    verify(taxaCambioRepositoryMock, times(1))
        .findFirstByMoedaOrigemAndMoedaDestinoOrderByCreatedAtDesc("USD", "BRL");
  }

  @Test
  void deveLancarExcecaoQuandoATaxaDeCambioNaoForEncontrada() {
    BigDecimal valorOriginal = new BigDecimal("100.00");

    when(taxaCambioRepositoryMock.findFirstByMoedaOrigemAndMoedaDestinoOrderByCreatedAtDesc("EUR", "BRL"))
        .thenReturn(Optional.empty());

    Exception excecao = assertThrows(CurrencyTaxNotFoundException.class, () -> {
      cambioService.converter(valorOriginal, "EUR", "BRL");
    });

    assertTrue(excecao.getMessage().contains("Taxa de câmbio não encontrada"));
  }
}