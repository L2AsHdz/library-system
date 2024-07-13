package com.ayd2.librarysystem.auth.service;

import com.ayd2.librarysystem.auth.model.dto.CredentialsDto;
import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.ServiceException;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.dto.StudentRequestCreateDto;
import com.ayd2.librarysystem.user.model.dto.UserRequestCreateDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.repository.StudentRepository;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    StudentRepository studentRepository;

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
    private StudentModel studentModel;
    private UserRequestCreateDto userRequestCreateDto;
    private UserRequestCreateDto studentRequestDto;
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

        studentModel = new StudentModel();
        studentModel.setId(userModel.getId());
        studentModel.setFullName(userModel.getFullName());
        studentModel.setUsername(userModel.getUsername());
        studentModel.setEmail(userModel.getEmail());
        studentModel.setPassword(userModel.getPassword());
        studentModel.setUserRole(userModel.getUserRole());
        studentModel.setStatus(userModel.getStatus());
        studentModel.setAcademicNumber(123456L);
        studentModel.setCareerModel(CareerModel.builder().id(1L).name("Career 1").build());

        userRequestCreateDto = new UserRequestCreateDto(
                userModel.getFullName(),
                userModel.getUsername(),
                userModel.getEmail(),
                userModel.getPassword(),
                userModel.getBirthDate(),
                userModel.getUserRole().name()
        );

        studentRequestDto = new StudentRequestCreateDto(
                userModel.getFullName(),
                userModel.getUsername(),
                userModel.getEmail(),
                userModel.getPassword(),
                userModel.getBirthDate(),
                userModel.getUserRole().name(),
                studentModel.getAcademicNumber(),
                studentModel.getCareerModel().getId()
        );

        credentialsDto = new CredentialsDto(userModel.getUsername(), userModel.getPassword());
    }

    @Test
    public void itShouldCreateNewUser() throws DuplicatedEntityException {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(encoder.encode(any(String.class))).thenReturn(userModel.getPassword());
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        UserResponseDto response = authenticationService.signUp(userRequestCreateDto);

        assertThat(response).isNotNull().isInstanceOf(UserResponseDto.class);
        assertThat(response.getUsername()).isEqualTo(userModel.getUsername());
        verify(userRepository).findByUsername(any(String.class));
        verify(userRepository).findByEmail(any(String.class));
        verify(encoder).encode(any(String.class));
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    public void itShouldCreateNewStudent() throws DuplicatedEntityException {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(studentRepository.findByAcademicNumber(any(Long.class))).thenReturn(Optional.empty());
        when(encoder.encode(any(String.class))).thenReturn(studentModel.getPassword());
        when(studentRepository.save(any(StudentModel.class))).thenReturn(studentModel);

        UserResponseDto response = authenticationService.signUp(studentRequestDto);

        assertThat(response).isNotNull().isInstanceOf(UserResponseDto.class);
        assertThat(response.getUsername()).isEqualTo(userModel.getUsername());
        verify(userRepository).findByUsername(any(String.class));
        verify(userRepository).findByEmail(any(String.class));
        verify(encoder).encode(any(String.class));
        verify(studentRepository).save(any(StudentModel.class));
    }

    @Test
    public void itShouldThrowDuplicatedEntityException_WhenUsernameExists() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userModel));

        assertThatThrownBy(() -> authenticationService.signUp(userRequestCreateDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("User with username already exists");
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    public void itShouldThrowException_WhenEmailExists() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(userModel));

        assertThatThrownBy(() -> authenticationService.signUp(userRequestCreateDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("User with email already exists");
        verify(userRepository).findByUsername(any(String.class));
        verify(userRepository).findByEmail(any(String.class));
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    public void itShouldThrowException_WhenAcademicNumberExists() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        when(studentRepository.findByAcademicNumber(any(Long.class))).thenReturn(Optional.of(studentModel));

        assertThatThrownBy(() -> authenticationService.signUp(studentRequestDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("Student with academic number already exists");
        verify(userRepository).findByUsername(any(String.class));
        verify(userRepository).findByEmail(any(String.class));
        verify(studentRepository).findByAcademicNumber(any(Long.class));
        verify(studentRepository, never()).save(any(StudentModel.class));
    }

    @Test
    public void isShouldReturnToken_WhenCredentialsAreValid() throws IOException, ServiceException {
        var generatedToken = "GeneratedToken";

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userModel));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(userModel)).thenReturn(generatedToken);

        var token = authenticationService.signIn(credentialsDto);

        assertThat(token).isNotNull();
        assertThat(token).isEqualTo(generatedToken);

        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication, times(1)).isAuthenticated();
        verify(jwtService, times(1)).generateToken(userModel);
    }

    @Test
    public void itShouldThrowException_WhenCredentialsAreInvalid() throws IOException, ServiceException {
        var generatedToken = "GeneratedToken";

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userModel));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));

        assertThatThrownBy(() -> authenticationService.signIn(credentialsDto))
                .isInstanceOf(ServiceException.class)
                .hasMessage("Invalid credentials");

        verify(userRepository).findByUsername(credentialsDto.username());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void itShouldThrowException_WhenUserNotFound() throws IOException {
        when(userRepository.findByUsername(credentialsDto.username())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(credentialsDto.username())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.signIn(credentialsDto))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findByUsername(credentialsDto.username());
        verify(userRepository, times(1)).findByEmail(credentialsDto.username());
        verify(studentRepository, never()).findByAcademicNumber(any(Long.class));
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication, never()).isAuthenticated();
        verify(jwtService, never()).generateToken(any(UserModel.class));
    }

    @Test
    public void itShouldThrowException_WhenUserStatusIsInvalid() throws IOException {
        userModel.setStatus((short)0);
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(userModel));

        assertThatThrownBy(() -> authenticationService.signIn(credentialsDto))
                .isInstanceOf(ServiceException.class)
                .hasMessage("User is disabled");

        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(studentRepository, never()).findByAcademicNumber(any(Long.class));
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication, never()).isAuthenticated();
        verify(jwtService, never()).generateToken(any(UserModel.class));
    }

    /*
        TODO: Implement the following tests
        - itShouldThrowException_WhenStudentNotFound
     */

//    @Test
//    public void testSignIn_ShouldThrowException_WhenAuthenticationFails() throws AuthenticationException {
//        when(userRepository.findByUsername(credentialsDto.username())).thenReturn(Optional.of(userModel));
//        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {});
//
//        assertThrows(ServiceException.class, () -> authenticationService.signIn(credentialsDto));
//    }
}