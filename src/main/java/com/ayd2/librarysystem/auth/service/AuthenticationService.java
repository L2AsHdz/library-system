package com.ayd2.librarysystem.auth.service;

import com.ayd2.librarysystem.auth.model.dto.CredentialsDto;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.ServiceException;
import com.ayd2.librarysystem.user.model.dto.StudentRequestCreateDto;
import com.ayd2.librarysystem.user.model.dto.UserRequestCreateDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import com.ayd2.librarysystem.user.repository.StudentRepository;
import com.ayd2.librarysystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    public UserResponseDto signUp(UserRequestCreateDto userRequestCreateDto) throws DuplicatedEntityException {
        var duplicatedUserByUsername = userRepository.findByUsername(userRequestCreateDto.getUsername());
        if (duplicatedUserByUsername.isPresent())
            throw new DuplicatedEntityException("User with username already exists");

        var duplicatedUserByEmail = userRepository.findByEmail(userRequestCreateDto.getEmail());
        if (duplicatedUserByEmail.isPresent())
            throw new DuplicatedEntityException("User with email already exists");

        if (userRequestCreateDto instanceof StudentRequestCreateDto studentRequestDto) {
            var duplicatedStudentByAcademicNumber = studentRepository.findByAcademicNumber(studentRequestDto.academicNumber());
            if (duplicatedStudentByAcademicNumber.isPresent())
                throw new DuplicatedEntityException("Student with academic number already exists");

            var newStudent = studentRequestDto.toEntity();
            newStudent.setPassword(encoder.encode(newStudent.getPassword()));
            studentRepository.save(newStudent);
            return newStudent.toRecord();
        }

        var newUser = userRequestCreateDto.toEntity();
        newUser.setPassword(encoder.encode(newUser.getPassword()));

        var userSaved = userRepository.save(newUser);
        return userSaved.toRecord();
    }

    public String signIn(CredentialsDto credentialsDto) throws IOException, ServiceException {
        var user = userRepository.findByUsername(credentialsDto.username())
                .orElseGet(() -> userRepository.findByEmail(credentialsDto.username())
                        .orElse(null));

        if (Objects.isNull(user)) {
            try {
                var academicNumber = Long.parseLong(credentialsDto.username());
                user = studentRepository.findByAcademicNumber(academicNumber)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            } catch (NumberFormatException e) {
                throw new UsernameNotFoundException("User not found");
            }
        }

        if (!(user.getStatus() == 1))
            throw new ServiceException("User is disabled");

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), credentialsDto.password())
        );

        if (authentication.isAuthenticated())
            return jwtService.generateToken(user);
        else
            throw new ServiceException("Invalid credentials");
    }
}
