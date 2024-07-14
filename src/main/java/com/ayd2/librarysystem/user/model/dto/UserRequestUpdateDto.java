package com.ayd2.librarysystem.user.model.dto;

import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link UserModel}
 */
@Getter
public class UserRequestUpdateDto implements Serializable {

        @NotBlank(message = "The name is required")
        @Size(min = 3, max = 150, message = "The name must be between 15 and 150 characters")
        private final String fullName;

        @NotBlank(message = "The username is required")
        @Size(min = 3, max = 50, message = "The username must be between 3 and 50 characters")
        private final String username;

        @NotBlank(message = "The email is required")
        @Email(message = "The email is not valid")
        @Size(min = 3, max = 100, message = "The email must be between 3 and 100 characters")
        private final String email;

        @NotNull(message = "The birth date is required")
        @Past(message = "The birth date must be in the past")
        private final LocalDate birthDate;

        @NotBlank(message = "The user role is required")
        @Pattern(regexp = "^(ADMIN|STUDENT|LIBRARIAN)$", message = "The user role must be ADMIN, STUDENT or LIBRARIAN")
        private final String userRole;

        public UserRequestUpdateDto(String fullName, String username, String email, LocalDate birthDate, String userRole) {
                this.fullName = fullName;
                this.username = username;
                this.email = email;
                this.birthDate = birthDate;
                this.userRole = userRole;

        }

        public UserModel toEntity() {
                return UserModel.builder()
                        .fullName(fullName)
                        .username(username)
                        .email(email)
                        .birthDate(birthDate)
                        .userRole(Rol.valueOf(userRole.toUpperCase()))
                        .status((short) 1)
                        .build();
        }
}