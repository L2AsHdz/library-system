package com.ayd2.librarysystem.user.service;

import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.dto.UserRequestCreateDto;
import com.ayd2.librarysystem.user.model.dto.UserRequestUpdateDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import com.ayd2.librarysystem.user.model.dto.UserUpdatePasswordDto;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getUserById(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .map(UserModel::toRecord)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAllByUserRoleIn(List.of(Rol.ADMIN, Rol.LIBRARIAN)).stream()
                .map(UserModel::toRecord)
                .toList();
    }

    public UserResponseDto updateUser(Long id, UserRequestUpdateDto userRequestUpdateDto) throws NotFoundException, DuplicatedEntityException {
        var userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var duplicatedByEmail = userRepository.findByEmailAndIdNot(userRequestUpdateDto.getEmail(), id);
        if (duplicatedByEmail.isPresent())
            throw new DuplicatedEntityException("User with this email already exists");

        var duplicatedByUsername = userRepository.findByUsernameAndIdNot(userRequestUpdateDto.getUsername(), id);
        if (duplicatedByUsername.isPresent())
            throw new DuplicatedEntityException("User with this username already exists");

        userToUpdate.setFullName(userRequestUpdateDto.getFullName());
        userToUpdate.setUsername(userRequestUpdateDto.getUsername());
        userToUpdate.setEmail(userRequestUpdateDto.getEmail());
        userToUpdate.setBirthDate(userRequestUpdateDto.getBirthDate());

        var updatedUser = userRepository.save(userToUpdate);
        return updatedUser.toRecord();
    }

    public UserResponseDto updatePassword(Long id, UserUpdatePasswordDto userUpdatePasswordDto) throws NotFoundException, DuplicatedEntityException {
        var userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userToUpdate.setPassword(userUpdatePasswordDto.newPassword());

        var updatedUser = userRepository.save(userToUpdate);
        return updatedUser.toRecord();
    }

}
