package com.ayd2.librarysystem.auth.service;

import com.ayd2.librarysystem.auth.model.dto.CredentialsDto;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.ServiceException;
import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.dto.UserRequestDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    Authentication authentication;

    @Mock
    JwtService jwtService;

    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    AuthenticationService authenticationService;

    private UserModel userModel;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private CredentialsDto credentialsDto;

    @BeforeEach
    public void setUp() {
        userModel = UserModel.builder()
                .id(1L)
                .fullName("Test User")
                .username("testuser")
                .email("testuser@email.com")
                .password("password")
                .userRole(Rol.LIBRARIAN)
                .status((short)1)
                .build();

        userRequestDto = new UserRequestDto(
                userModel.getFullName(),
                userModel.getUsername(),
                userModel.getEmail(),
                userModel.getPassword(),
                userModel.getBirthDate(),
                userModel.getUserRole().name()
        );
        userResponseDto = userModel.toRecord();

        credentialsDto = new CredentialsDto(userModel.getUsername(), userModel.getEmail(), userModel.getPassword());
    }

    @Test
    public void itShouldCreateNewUser() throws DuplicatedEntityException {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userRequestDto.email())).thenReturn(Optional.empty());
        when(encoder.encode(userRequestDto.password())).thenReturn(userModel.getPassword());
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        UserResponseDto response = authenticationService.signUp(userRequestDto);

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(userModel.getUsername());
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    public void itShouldThrowException_WhenUsernameExists() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.of(userModel));

        assertThrows(DuplicatedEntityException.class, () -> authenticationService.signUp(userRequestDto));
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    public void itShouldThrowException_WhenEmailExists() {
        when(userRepository.findByUsername(userRequestDto.username())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userRequestDto.email())).thenReturn(Optional.of(userModel));

        assertThrows(DuplicatedEntityException.class, () -> authenticationService.signUp(userRequestDto));
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    public void isShouldReturnToken_WhenCredentialsAreValid() throws IOException, ServiceException {
        var generatedToken = "GeneratedToken";

        when(userRepository.findByUsername(credentialsDto.username())).thenReturn(Optional.of(userModel));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(userModel)).thenReturn(generatedToken);

        var token = authenticationService.signIn(credentialsDto);

        assertThat(token).isNotNull();
        assertThat(token).isEqualTo(generatedToken);
    }

    @Test
    public void itShouldThrowException_WhenCredentialsAreInvalid() throws IOException, ServiceException {
        var generatedToken = "GeneratedToken";

        when(userRepository.findByUsername(credentialsDto.username())).thenReturn(Optional.of(userModel));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));

        assertThrows(ServiceException.class, () -> authenticationService.signIn(credentialsDto));
    }

    @Test
    public void itShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername(credentialsDto.username())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(credentialsDto.email())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.signIn(credentialsDto));
    }

    @Test
    public void itShouldThrowException_WhenUserStatusIsInvalid() {
        userModel.setStatus((short)0);
        when(userRepository.findByUsername(credentialsDto.username())).thenReturn(Optional.of(userModel));

        assertThrows(ServiceException.class, () -> authenticationService.signIn(credentialsDto));
    }

//    @Test
//    public void testSignIn_ShouldThrowException_WhenAuthenticationFails() throws AuthenticationException {
//        when(userRepository.findByUsername(credentialsDto.username())).thenReturn(Optional.of(userModel));
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {});
//
//        assertThrows(ServiceException.class, () -> authenticationService.signIn(credentialsDto));
//    }
}