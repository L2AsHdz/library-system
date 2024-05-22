package com.ayd2.librarysystem.auth.controller;

import com.ayd2.librarysystem.auth.model.dto.CredentialsDto;
import com.ayd2.librarysystem.auth.service.AuthenticationService;
import com.ayd2.librarysystem.exception.ServiceException;
import com.ayd2.librarysystem.user.model.dto.UserRequestDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<Void> getToken(@RequestBody @Valid CredentialsDto credentials) throws ServiceException, IOException {
        var token = authenticationService.signIn(credentials);
        var headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody @Valid UserRequestDto userRequestDto) throws ServiceException {
        var userCreated = authenticationService.signUp(userRequestDto);
        return ResponseEntity.ok().body(userCreated);
    }
}
