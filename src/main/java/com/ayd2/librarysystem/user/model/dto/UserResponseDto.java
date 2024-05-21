package com.ayd2.librarysystem.user.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.ayd2.librarysystem.user.model.UserModel}
 */
public record UserResponseDto(Long id, String fullName, String username, String email, String password,
                              LocalDate birthDate, String userRole, Boolean status) implements Serializable {
}