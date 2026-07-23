package com.br.srm.asset.test.domain;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taxas_cambio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxaCambio {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "A moeda de origem é obrigatória")
  @Column(nullable = false, length = 10)
  private String moedaOrigem;

  @NotBlank(message = "A moeda de destino é obrigatória")
  @Column(nullable = false, length = 10)
  private String moedaDestino;

  @NotNull(message = "O fator de conversão é obrigatório")
  @Positive(message = "O fator de conversão deve ser maior que zero")
  @Column(nullable = false, precision = 18, scale = 6)
  private BigDecimal fatorConversao;

  @Column(nullable = false, updatable = false)
  private Instant createdAt;

}