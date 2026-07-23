package com.br.srm.asset.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.br.srm.asset.test.domain.Recebivel;

import jakarta.persistence.LockModeType;

public interface RecebivelRepository extends JpaRepository<Recebivel, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT r FROM Recebivel r WHERE r.id = :id")
  Optional<Recebivel> findByIdForUpdate(Long id);
}
