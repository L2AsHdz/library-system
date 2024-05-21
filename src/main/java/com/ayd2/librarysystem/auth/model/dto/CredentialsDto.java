package com.ayd2.librarysystem.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link com.ayd2.librarysystem.user.model.UserModel}
 */
public record CredentialsDto(

        String username,
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Password is required")
        String password
) implements Serializable {
}