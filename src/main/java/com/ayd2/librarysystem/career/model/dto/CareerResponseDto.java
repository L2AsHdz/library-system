package com.ayd2.librarysystem.career.model.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.ayd2.librarysystem.career.model.CareerModel}
 */
public record CareerResponseDto(Long id, String name) implements Serializable {
}