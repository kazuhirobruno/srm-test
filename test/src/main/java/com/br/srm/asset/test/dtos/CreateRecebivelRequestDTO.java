package com.br.srm.asset.test.dtos;

import java.math.BigDecimal;

import com.br.srm.asset.test.domain.TipoRecebivel;

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
public class CreateRecebivelRequestDTO {

  @NotNull(message = "O valor original é obrigatório")
  @Positive(message = "O valor original deve ser maior que zero")
  private BigDecimal valorOriginal;

  @NotNull(message = "O prazo é obrigatório")
  @Positive(message = "O prazo deve ser maior que zero")
  private Integer prazo;

  @NotNull(message = "O tipo de recebível é obrigatório (DUPLICATA ou CHEQUE)")
  private TipoRecebivel tipo;

  @NotBlank(message = "A moeda original é obrigatória")
  private String moedaOriginal;

  @NotBlank(message = "O nome do cedente é obrigatório")
  private String cedente;
}