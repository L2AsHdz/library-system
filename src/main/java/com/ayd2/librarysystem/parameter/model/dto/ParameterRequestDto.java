package com.ayd2.librarysystem.parameter.model.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.ayd2.librarysystem.parameter.model.ParameterModel}
 */
public record ParameterRequestDto(
        @NotNull(message = "The parameter id is required")
        Long id,
        @NotNull(message = "The parameter name is required")
        Double parameterValue) implements Serializable {
}