package com.br.srm.asset.test.strategy;

import static org.junit.jupiter.api.Assertions.*;

import com.br.srm.asset.test.domain.TipoRecebivel;
import com.br.srm.asset.test.exceptions.StrategyNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class PrecificacaoFactoryTest {

  @Mock
  private PrecificacaoStrategy duplicataStrategyMock;

  @Mock
  private PrecificacaoStrategy chequeStrategyMock;

  private PrecificacaoFactory factory;

  @BeforeEach
  void setUp() {
    Map<String, PrecificacaoStrategy> strategiesMap = new HashMap<>();
    strategiesMap.put("DUPLICATA", duplicataStrategyMock);
    strategiesMap.put("CHEQUE", chequeStrategyMock);

    factory = new PrecificacaoFactory(strategiesMap);
  }

  @Test
  void deveRetornarEstrategiaDeDuplicataQuandoTipoForDuplicata() {
    PrecificacaoStrategy resultado = factory.obterStrategy(TipoRecebivel.DUPLICATA);

    assertNotNull(resultado);
    assertEquals(duplicataStrategyMock, resultado);
  }

  @Test
  void deveRetornarEstrategiaDeChequeQuandoTipoForCheque() {
    PrecificacaoStrategy resultado = factory.obterStrategy(TipoRecebivel.CHEQUE);

    assertNotNull(resultado);
    assertEquals(chequeStrategyMock, resultado);
  }

  @Test
  void deveLancarExcecaoQuandoTipoNaoEstiverMapeadoNoMapa() {
    PrecificacaoFactory factoryVazia = new PrecificacaoFactory(new HashMap<>());

    assertThrows(StrategyNotFoundException.class, () -> {
      factoryVazia.obterStrategy(TipoRecebivel.DUPLICATA);
    });
  }
}