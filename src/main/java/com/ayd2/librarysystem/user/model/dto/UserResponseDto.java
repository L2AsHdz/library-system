package com.ayd2.librarysystem.user.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.ayd2.librarysystem.user.model.UserModel}
 */
public class UserResponseDto implements Serializable {
        private final Long id;
        private final String fullName;
        private final String username;
        private final String email;
        private final String password;
        private final LocalDate birthDate;
        private final String userRole;
        private final Boolean status;

        public UserResponseDto(Long id, String fullName, String username, String email, String password,
                               LocalDate birthDate, String userRole, Boolean status) {
                this.id = id;
                this.fullName = fullName;
                this.username = username;
                this.email = email;
                this.password = password;
                this.birthDate = birthDate;
                this.userRole = userRole;
                this.status = status;
        }

        public Long getId() {
                return id;
        }

        public String getFullName() {
                return fullName;
        }

        public String getUsername() {
                return username;
        }

        public String getEmail() {
                return email;
        }

        public String getPassword() {
                return password;
        }

        public LocalDate getBirthDate() {
                return birthDate;
        }

        public String getUserRole() {
                return userRole;
        }

        public Boolean getStatus() {
                return status;
        }
}