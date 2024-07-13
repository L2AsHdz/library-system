package com.ayd2.librarysystem.auth.service;

import com.ayd2.librarysystem.auth.model.UserModelDetails;
import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.repository.StudentRepository;
import com.ayd2.librarysystem.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UserModel user;
    private StudentModel student;

    @BeforeEach
    void setUp() {
        user = UserModel.builder()
                .id(1L)
                .email("email")
                .userRole(Rol.LIBRARIAN)
                .password("password")
                .status((short)1)
                .build();

        student = new StudentModel();
        student.setId(user.getId());
        student.setEmail(user.getEmail());
        student.setUserRole(user.getUserRole());
        student.setPassword(user.getPassword());
        student.setStatus(user.getStatus());
        student.setCareerModel(CareerModel.builder().id(1L).name("Career 1").build());
        student.setAcademicNumber(1234567890L);
        student.setIsSanctioned(false);
    }

    @Test
    public void itShouldLoadUserDetailsByUsername() {
        user.setUsername("username");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var result = customUserDetailsService.loadUserByUsername(user.getUsername());

        assertThat(result).isNotNull()
                .isInstanceOf(UserModelDetails.class);

        verify(userRepository).findByUsername(anyString());
    }

    @Test
    public void itShouldLoadStudentDetailsByUsername() {
        when(studentRepository.findByAcademicNumber(anyLong())).thenReturn(Optional.of(student));

        var result = customUserDetailsService.loadUserByUsername(student.getAcademicNumber().toString());

        assertThat(result).isNotNull()
                .isInstanceOf(UserModelDetails.class);

        verify(studentRepository).findByAcademicNumber(anyLong());
    }

    @Test
    public void itShouldLoadUserDetailsByEmail() {
        user.setEmail("email");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        var result = customUserDetailsService.loadUserByUsername(user.getEmail());

        assertThat(result).isNotNull()
                .isInstanceOf(UserModelDetails.class);

        verify(userRepository).findByEmail(anyString());
    }

    @Test
    public void itShouldLoadUserDetailByAcademicNumber() {
        when(studentRepository.findByAcademicNumber(anyLong())).thenReturn(Optional.of(student));

        var result = customUserDetailsService.loadUserByUsername(student.getAcademicNumber().toString());

        assertThat(result).isNotNull()
                .isInstanceOf(UserModelDetails.class);

        verify(studentRepository).findByAcademicNumber(anyLong());
    }

    @Test
    public void itShouldThrowUsernameNotFoundException_WhenUserNotFound() {
        user.setUsername("username");
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(user.getUsername()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(studentRepository, never()).findByAcademicNumber(anyLong());
    }

    @Test
    public void itShouldThrowUsernameNotFoundException_WhenStudentNotFound() {
        when(studentRepository.findByAcademicNumber(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(student.getAcademicNumber().toString()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(studentRepository, times(1)).findByAcademicNumber(anyLong());
    }
}