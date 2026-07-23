package com.br.srm.asset.test.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "recebiveis")
public class Recebivel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, precision = 18, scale = 4)
  private BigDecimal valorOriginal;

  @Column(nullable = false)
  private Integer prazo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoRecebivel tipo;

  @Column(nullable = false)
  private String moedaOriginal;

  @Column(nullable = false)
  private String status;

  @Column(precision = 18, scale = 4)
  private BigDecimal valorLiquidado;

  @Column(length = 10)
  private String moedaLiquidacao;

  @Column(updatable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;
}
