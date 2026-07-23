package com.br.srm.asset.test.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.br.srm.asset.test.domain.Recebivel;
import com.br.srm.asset.test.dtos.CreateRecebivelRequestDTO;
import com.br.srm.asset.test.exceptions.SettledTransactionException;
import com.br.srm.asset.test.exceptions.TransactionNotFoundException;
import com.br.srm.asset.test.repository.RecebivelRepository;
import com.br.srm.asset.test.strategy.PrecificacaoFactory;
import com.br.srm.asset.test.strategy.PrecificacaoStrategy;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecebivelService {

  private final RecebivelRepository recebivelRepository;
  private final PrecificacaoFactory precificacaoFactory;
  private final CambioService cambioService;

  @Transactional
  public Recebivel cadastrarRecebivel(CreateRecebivelRequestDTO recebivel) {
    Recebivel response = Recebivel
        .builder()
        .moedaOriginal(recebivel.getMoedaOriginal())
        .prazo(recebivel.getPrazo())
        .valorOriginal(recebivel.getValorOriginal())
        .tipo(recebivel.getTipo())
        .status("PENDENTE")
        .build();
    return recebivelRepository.save(response);
  }

  public BigDecimal simularPrecificacao(Recebivel recebivel, String moedaPagamento, BigDecimal taxaBase) {
    PrecificacaoStrategy strategy = precificacaoFactory.obterStrategy(recebivel.getTipo());

    BigDecimal valorPresente = strategy.calcularValorPresente(
        recebivel.getValorOriginal(),
        recebivel.getPrazo(),
        taxaBase);

    return cambioService.converter(valorPresente, recebivel.getMoedaOriginal(), moedaPagamento);
  }

  @Transactional
  public void liquidar(Long id, String moedaPagamento, BigDecimal taxaBase) {
    Recebivel recebivel = recebivelRepository.findByIdForUpdate(id)
        .orElseThrow(() -> new TransactionNotFoundException("Recebível não encontrado com ID: " + id));

    if ("LIQUIDADO".equalsIgnoreCase(recebivel.getStatus())) {
      throw new SettledTransactionException("Operação negada: Este recebível já foi liquidado!");
    }

    BigDecimal valorLiquidoFinal = simularPrecificacao(recebivel, moedaPagamento, taxaBase);

    recebivel.setValorLiquidado(valorLiquidoFinal);
    recebivel.setMoedaLiquidacao(moedaPagamento.toUpperCase());
    recebivel.setStatus("LIQUIDADO");

    recebivelRepository.save(recebivel);
  }
}