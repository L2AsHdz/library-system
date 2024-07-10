package com.ayd2.librarysystem.user.service;

import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private UserModel testUser;
    private UserRequestDto testUserRequestDto;
    private UserResponseDto testUserResponseDto;

    @BeforeEach
    public void setUp() {
        testUser = UserModel.builder()
                .id(1L)
                .fullName("Test User")
                .username("testuser")
                .password("testpassword")
                .userRole(Rol.LIBRARIAN)
                .email("testuser@email.com")
                .status((short)1)
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();

        testUserRequestDto = new UserRequestDto(
                testUser.getFullName(),
                testUser.getUsername(),
                testUser.getEmail(),
                testUser.getPassword(),
                testUser.getBirthDate(),
                testUser.getUserRole().name()
        );

        testUserResponseDto = testUser.toRecord();
    }

    @Test
    public void itShouldGetUserById() throws NotFoundException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        var result = userService.getUserById(testUser.getId());

        assertThat(result).isNotNull().isEqualToComparingFieldByField(testUserResponseDto);

        verify(userRepository).findById(anyLong());
    }

    @Test
    public void itShouldThrowNotFoundExceptionWhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(testUser.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(anyLong());
    }

    @Test
    public void itShouldGetAllUsers() {
        when(userRepository.findAllByUserRoleIn(any())).thenReturn(List.of(testUser));

        var result = userService.getAllUsers();

        assertThat(result).isNotNull()
                .isNotEmpty()
                .hasSize(1);

        verify(userRepository).findAllByUserRoleIn(any());
    }

    @Test
    public void itShouldFindEmptyList_WhenNoUsers() {
        when(userRepository.findAllByUserRoleIn(any())).thenReturn(List.of());

        var result = userService.getAllUsers();

        assertThat(result).isNotNull()
                .isEmpty();

        verify(userRepository).findAllByUserRoleIn(any());
    }

    @Test
    public void itShouldUpdateUser() throws NotFoundException, DuplicatedEntityException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmailAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        when(userRepository.findByUsernameAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserModel.class))).thenReturn(testUser);

        var result = userService.updateUser(testUser.getId(), testUserRequestDto);

        assertThat(result).isNotNull().isEqualToComparingFieldByField(testUserResponseDto);

        verify(userRepository).findByEmailAndIdNot(anyString(), anyLong());
        verify(userRepository).findByUsernameAndIdNot(anyString(), anyLong());
        verify(userRepository).findById(anyLong());
        verify(userRepository).save(testUser);
    }

    @Test
    public void itShouldThrowNotFoundException_WhenUserToUpdateDoesntExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(testUser.getId(), testUserRequestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(anyLong());
    }

    @Test
    public void itShouldThrowDuplicatedEntityException_WhenEmailAlreadyExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmailAndIdNot(anyString(), anyLong())).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.updateUser(testUser.getId(), testUserRequestDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("User with this email already exists");

        verify(userRepository).findById(anyLong());
        verify(userRepository).findByEmailAndIdNot(anyString(), anyLong());
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    public void itShouldThrowDuplicatedEntityException_WhenUsernameAlreadyExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmailAndIdNot(anyString(), anyLong())).thenReturn(Optional.empty());
        when(userRepository.findByUsernameAndIdNot(anyString(), anyLong())).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.updateUser(testUser.getId(), testUserRequestDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("User with this username already exists");

        verify(userRepository).findById(anyLong());
        verify(userRepository).findByEmailAndIdNot(anyString(), anyLong());
        verify(userRepository).findByUsernameAndIdNot(anyString(), anyLong());
        verify(userRepository, never()).save(any(UserModel.class));
    }
}