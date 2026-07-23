package com.br.srm.asset.test.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponseDTO {
  private Instant timestamp;
  private Integer status;
  private String error;
  private Map<String, String> fields;
}
