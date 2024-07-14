package com.ayd2.librarysystem.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdatePasswordDto(
        @NotBlank(message = "The password is required")
        @Size(min = 3, message = "The password must be between 8 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "The password must contain at least one uppercase letter, one lowercase letter and one number")
        String newPassword
) {}
