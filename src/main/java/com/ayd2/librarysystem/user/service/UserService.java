package com.ayd2.librarysystem.user.service;

import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.dto.UserRequestDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
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
        return userRepository.findAll().stream()
                .map(UserModel::toRecord)
                .toList();
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) throws NotFoundException, DuplicatedEntityException {
        var userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var duplicatedByEmail = userRepository.findByEmail(userRequestDto.email());
        if (duplicatedByEmail.isPresent())
            throw new DuplicatedEntityException("User with this email already exists");

        var duplicatedByUsername = userRepository.findByUsername(userRequestDto.username());
        if (duplicatedByUsername.isPresent())
            throw new DuplicatedEntityException("User with this username already exists");

        userToUpdate.setFullName(userRequestDto.fullName());
        userToUpdate.setUsername(userRequestDto.username());
        userToUpdate.setPassword(userRequestDto.password());
        userToUpdate.setEmail(userRequestDto.email());
        userToUpdate.setBirthDate(userRequestDto.birthDate());

        var updatedUser = userRepository.save(userToUpdate);
        return updatedUser.toRecord();
    }

}
