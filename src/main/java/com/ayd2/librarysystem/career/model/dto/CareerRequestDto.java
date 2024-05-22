package com.ayd2.librarysystem.career.model.dto;

import com.ayd2.librarysystem.career.model.CareerModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.ayd2.librarysystem.career.model.CareerModel}
 */
public record CareerRequestDto(
        @Size(max = 150, message = "The name must be less than 150 characters")
        @NotBlank(message = "The name is required")
        String name
) implements Serializable {

    public CareerModel toEntity() {
        return new CareerModel(null, name);
    }
}