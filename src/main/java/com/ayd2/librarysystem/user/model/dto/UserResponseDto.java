package com.ayd2.librarysystem.user.model.dto;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.ayd2.librarysystem.user.model.UserModel}
 */
@Getter
public class UserResponseDto implements Serializable {
        private final Long id;
        private final String fullName;
        private final String username;
        private final String email;
        private final LocalDate birthDate;
        private final String userRole;
        private final Boolean status;

        public UserResponseDto(Long id, String fullName, String username, String email,
                               LocalDate birthDate, String userRole, Boolean status) {
                this.id = id;
                this.fullName = fullName;
                this.username = username;
                this.email = email;
                this.birthDate = birthDate;
                this.userRole = userRole;
                this.status = status;
        }
}