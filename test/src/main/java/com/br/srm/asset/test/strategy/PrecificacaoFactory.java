package com.br.srm.asset.test.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.br.srm.asset.test.domain.TipoRecebivel;
import com.br.srm.asset.test.exceptions.StrategyNotFoundException;

@Component
public class PrecificacaoFactory {

  private final Map<String, PrecificacaoStrategy> strategies;

  public PrecificacaoFactory(Map<String, PrecificacaoStrategy> strategies) {
    this.strategies = strategies;
  }

  public PrecificacaoStrategy obterStrategy(TipoRecebivel tipo) {
    PrecificacaoStrategy strategy = strategies.get(tipo.name());
    if (strategy == null) {
      throw new StrategyNotFoundException("Estratégia de precificação não encontrada para o tipo: " + tipo);
    }
    return strategy;
  }
}