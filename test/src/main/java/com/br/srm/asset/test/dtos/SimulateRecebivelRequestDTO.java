package com.br.srm.asset.test.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

import com.br.srm.asset.test.domain.TipoRecebivel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulateRecebivelRequestDTO {

  @NotNull(message = "O valor original é obrigatório")
  @Positive(message = "O valor original deve ser maior que zero")
  private BigDecimal valorOriginal;

  @NotNull(message = "O prazo é obrigatório")
  @Positive(message = "O prazo deve ser maior que zero")
  private Integer prazo;

  @NotNull(message = "O tipo de recebível é obrigatório (DUPLICATA ou CHEQUE)")
  private TipoRecebivel tipo;

  @NotBlank(message = "A moeda original é obrigatória")
  @Size(min = 3, max = 3, message = "A moeda original deve ter exatamente 3 caracteres")
  private String moedaOriginal;

  @NotBlank(message = "A moeda de pagamento é obrigatória")
  @Size(min = 3, max = 3, message = "A moeda de pagamento deve ter exatamente 3 caracteres")
  private String moedaPagamento;

  @NotNull(message = "A taxa base é obrigatória")
  @Positive(message = "A taxa base deve ser maior que zero")
  private BigDecimal taxaBase;
}