package com.br.srm.asset.test.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaxaCambioRequestDTO {
  @NotBlank(message = "A moeda de origem é obrigatória")
  @Size(min = 3, max = 3, message = "A moeda de origem deve ter exatamente 3 caracteres (Ex: USD)")
  private String moedaOrigem;

  @NotBlank(message = "A moeda de destino é obrigatória")
  @Size(min = 3, max = 3, message = "A moeda de destino deve ter exatamente 3 caracteres (Ex: BRL)")
  private String moedaDestino;

  @NotNull(message = "O fator de conversão é obrigatório")
  @Positive(message = "O fator de conversão deve ser maior que zero")
  private BigDecimal fatorConversao;
}