package com.br.srm.asset.test.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

import com.br.srm.asset.test.domain.TipoRecebivel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulateRecebivelRequestDTO {
  private BigDecimal valorOriginal;
  private Integer prazo;
  private TipoRecebivel tipo;
  private String moedaOriginal;
  private String moedaPagamento;
  private BigDecimal taxaBase; // Taxa Base vindo dinamicamente da tela do operador
}