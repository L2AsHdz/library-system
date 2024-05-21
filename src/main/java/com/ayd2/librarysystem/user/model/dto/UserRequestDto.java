package com.ayd2.librarysystem.user.model.dto;

import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.ayd2.librarysystem.user.model.UserModel}
 */
public record UserRequestDto(
        @NotBlank(message = "The name is required")
        @Size(min = 3, max = 150, message = "The name must be between 15 and 150 characters")
        String fullName,

        @NotBlank(message = "The username is required")
        @Size(min = 3, max = 50, message = "The username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "The email is required")
        @Email(message = "The email is not valid")
        @Size(min = 3, max = 100, message = "The email must be between 3 and 100 characters")
        String email,

        @NotBlank(message = "The password is required")
        @Size(min = 3, message = "The password must be between 8 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "The password must contain at least one uppercase letter, one lowercase letter and one number")
        String password,

        @NotNull(message = "The birth date is required")
        @Past(message = "The birth date must be in the past")
        LocalDate birthDate,

        @NotBlank(message = "The user role is required")
        @Pattern(regexp = "^(ADMIN|STUDENT|LIBRARIAN)$", message = "The user role must be ADMIN, STUDENT or LIBRARIAN")
        String role
) implements Serializable {


        public UserModel toEntity() {
                return UserModel.builder()
                        .fullName(fullName)
                        .username(username)
                        .email(email)
                        .password(password)
                        .birthDate(birthDate)
                        .userRole(Rol.valueOf(role.toUpperCase()))
                        .build();
        }
}