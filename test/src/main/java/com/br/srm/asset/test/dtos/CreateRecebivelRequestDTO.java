package com.br.srm.asset.test.dtos;

import java.math.BigDecimal;

import com.br.srm.asset.test.domain.TipoRecebivel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecebivelRequestDTO {
    private BigDecimal valorOriginal;
    private Integer prazo;
    private TipoRecebivel tipo;
    private String moedaOriginal;
}