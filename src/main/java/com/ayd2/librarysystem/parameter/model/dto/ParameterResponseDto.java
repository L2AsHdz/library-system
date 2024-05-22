package com.ayd2.librarysystem.parameter.model.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ayd2.librarysystem.parameter.model.ParameterModel}
 */
public record ParameterResponseDto(Long id, String parameterName, Double parameterValue) implements Serializable {
}