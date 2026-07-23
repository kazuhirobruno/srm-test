package com.br.srm.asset.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.srm.asset.test.domain.TaxaCambio;

public interface TaxaCambioRepository extends JpaRepository<TaxaCambio, Long> {

  Optional<TaxaCambio> findFirstByMoedaOrigemAndMoedaDestinoOrderByCreatedAtDesc(
      String moedaOrigem,
      String moedaDestino);
}