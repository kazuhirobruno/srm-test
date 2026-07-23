package com.br.srm.asset.test.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLiquidacaoRequestDTO {

  @NotBlank(message = "A moeda de pagamento é obrigatória")
  private String moedaPagamento;

  @NotNull(message = "A taxa base é obrigatória")
  @Positive(message = "A taxa base deve ser maior que zero")
  private BigDecimal taxaBase;
}