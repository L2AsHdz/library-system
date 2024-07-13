package com.ayd2.librarysystem.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.ayd2.librarysystem.AbstractMvcTest;
import com.ayd2.librarysystem.auth.model.dto.CredentialsDto;
import com.ayd2.librarysystem.auth.service.AuthenticationService;
import com.ayd2.librarysystem.exception.GlobalExceptionHandler;
import com.ayd2.librarysystem.user.model.dto.UserRequestCreateDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import com.ayd2.librarysystem.user.model.enums.Rol;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {AuthController.class, GlobalExceptionHandler.class})
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest extends AbstractMvcTest {

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void itShouldReturnToken() throws Exception {
        var credentials = new CredentialsDto("username", "123");
        var token = "dummyToken";

        when(authenticationService.signIn(any(CredentialsDto.class))).thenReturn(token);

        ResultActions result = mockMvc.perform(post("/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(credentials)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, "Bearer " + token));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void itShouldReturnCreatedUser() throws Exception {
        var userRequest = new UserRequestCreateDto(
                "Usuario Prueba",
                "username",
                "username@email.com",
                "Password_123",
                LocalDate.now().minusYears(15),
                Rol.ADMIN.toString()
        );

        var userResponse = new UserResponseDto(
                1L,
                "Usuario Prueba",
                "username",
                "username@email.com",
                LocalDate.now(),
                Rol.ADMIN.toString(),
                true
        );

        when(authenticationService.signUp(any(UserRequestCreateDto.class))).thenReturn(userResponse);

        ResultActions resultActions = mockMvc.perform(post("/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.username").value(userResponse.getUsername()))
        ;
    }
}
