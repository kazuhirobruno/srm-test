package com.br.srm.asset.test.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.br.srm.asset.test.domain.Recebivel;
import com.br.srm.asset.test.dtos.ExtratoLiquidacaoDTO;

import jakarta.persistence.LockModeType;

public interface RecebivelRepository extends JpaRepository<Recebivel, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT r FROM Recebivel r WHERE r.id = :id")
  Optional<Recebivel> findByIdForUpdate(Long id);

  @Query(value = "SELECT r.id as id, r.cedente as cedente, r.valor_original as valorOriginal, r.valor_liquidado as valorLiquidado, "
      +
      "r.moeda_original as moedaOriginal, r.moeda_liquidacao as moedaLiquidacao, " +
      "r.status as status, r.updated_at as updatedAt " +
      "FROM recebiveis r " +
      "WHERE (:cedente IS NULL OR r.cedente ILIKE %:cedente%) " +
      "AND (:moeda IS NULL OR r.moeda_original = :moeda OR r.moeda_liquidacao = :moeda) " +
      "AND (:dataInicio IS NULL OR r.updated_at >= :dataInicio) " +
      "AND (:dataFim IS NULL OR r.updated_at <= :dataFim)", countQuery = "SELECT count(*) FROM recebiveis r " +
          "WHERE (:cedente IS NULL OR r.cedente ILIKE %:cedente%) " +
          "AND (:moeda IS NULL OR r.moeda_original = :moeda OR r.moeda_liquidacao = :moeda) " +
          "AND (:dataInicio IS NULL OR r.updated_at >= :dataInicio) " +
          "AND (:dataFim IS NULL OR r.updated_at <= :dataFim)", nativeQuery = true)
  Page<ExtratoLiquidacaoDTO> obterExtratoAnalitico(
      @Param("cedente") String cedente,
      @Param("moeda") String moeda,
      @Param("dataInicio") Instant dataInicio,
      @Param("dataFim") Instant dataFim,
      Pageable pageable);
}
