package com.br.srm.asset.test.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

  @NotNull(message = "O valor original é obrigatório")
  @Positive(message = "O valor original deve ser maior que zero")
  @Column(nullable = false, precision = 18, scale = 4)
  private BigDecimal valorOriginal;

  @NotNull(message = "O prazo é obrigatório")
  @Positive(message = "O prazo deve ser maior que zero")
  @Column(nullable = false)
  private Integer prazo;

  @NotNull(message = "O tipo de recebível é obrigatório")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoRecebivel tipo;

  @NotBlank(message = "A moeda original é obrigatória")
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
