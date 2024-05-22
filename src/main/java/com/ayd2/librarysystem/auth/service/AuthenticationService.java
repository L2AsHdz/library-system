package com.ayd2.librarysystem.auth.service;

import com.ayd2.librarysystem.auth.model.dto.CredentialsDto;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.ServiceException;
import com.ayd2.librarysystem.user.model.dto.UserRequestDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import com.ayd2.librarysystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    public UserResponseDto signUp(UserRequestDto userRequestDto) throws DuplicatedEntityException {
        var duplicatedUserByUsername = userRepository.findByUsername(userRequestDto.username());
        if (duplicatedUserByUsername.isPresent())
            throw new DuplicatedEntityException("User with username already exists");

        var duplicatedUserByEmail = userRepository.findByEmail(userRequestDto.email());
        if (duplicatedUserByEmail.isPresent())
            throw new DuplicatedEntityException("User with email already exists");

        var newUser = userRequestDto.toEntity();
        newUser.setPassword(encoder.encode(newUser.getPassword()));

        var userSaved = userRepository.save(newUser);
        return userSaved.toRecord();
    }

    public String signIn(CredentialsDto credentialsDto) throws IOException, ServiceException {
        var user = userRepository.findByUsername(credentialsDto.username())
                .orElseGet(() -> userRepository.findByEmail(credentialsDto.email())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));


        if (!(user.getStatus() == 1))
            throw new ServiceException("User not found");

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), credentialsDto.password())
        );

        if (authentication.isAuthenticated())
            return jwtService.generateToken(user);
        else
            throw new ServiceException("Invalid credentials");
    }
}