package com.ayd2.librarysystem.auth.service;

import com.ayd2.librarysystem.auth.model.UserModelDetails;
import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UserModel user;

    @BeforeEach
    void setUp() {
        user = UserModel.builder()
                .id(1L)
                .email("email")
                .userRole(Rol.LIBRARIAN)
                .password("password")
                .status((short)1)
                .build();
    }



    @Test
    void itShouldLoadUserDetailsByUsername() {
        // Arrange
        user.setUsername("username");
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        // Act
        var result = customUserDetailsService.loadUserByUsername(user.getUsername());

        // Assert
        assertThat(result).isNotNull()
                .isInstanceOf(UserModelDetails.class);

    }

    @Test
    void itShouldLoadUserDetailsByEmail() {
        // Arrange
        user.setEmail("email");
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        // Act
        var result = customUserDetailsService.loadUserByUsername(user.getEmail());

        // Assert
        assertThat(result).isNotNull()
                .isInstanceOf(UserModelDetails.class);

    }
}