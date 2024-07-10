package com.ayd2.librarysystem.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.user.model.dto.UserRequestDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import com.ayd2.librarysystem.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserControllerDiffblueTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#getAllUsers()}
     */
    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/users");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserController#getUserById(Long)}
     */
    @Test
    void testGetUserById() throws Exception {
        // Arrange
        when(userService.getUserById(Mockito.<Long>any())).thenReturn(new UserResponseDto(1L, "Dr Jane Doe", "janedoe",
                "jane.doe@example.org", "iloveyou", LocalDate.of(1970, 1, 1), "User Role", true));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/users/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"fullName\":\"Dr Jane Doe\",\"username\":\"janedoe\",\"email\":\"jane.doe@example.org\",\"password\":"
                                        + "\"iloveyou\",\"birthDate\":[1970,1,1],\"userRole\":\"User Role\",\"status\":true}"));
    }

    /**
     * Method under test: {@link UserController#updateUser(Long, UserRequestDto)}
     */
    @Test
    void testUpdateUser() throws DuplicatedEntityException, NotFoundException {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        UserService userService = mock(UserService.class);
        UserResponseDto userResponseDto = new UserResponseDto(1L, "Dr Jane Doe", "janedoe", "jane.doe@example.org",
                "iloveyou", LocalDate.of(1970, 1, 1), "User Role", true);

        when(userService.updateUser(Mockito.<Long>any(), Mockito.<UserRequestDto>any())).thenReturn(userResponseDto);
        UserController userController = new UserController(userService);

        // Act
        ResponseEntity<UserResponseDto> actualUpdateUserResult = userController.updateUser(1L, new UserRequestDto(
                "Dr Jane Doe", "janedoe", "jane.doe@example.org", "iloveyou", LocalDate.of(1970, 1, 1), "User Role"));

        // Assert
        verify(userService).updateUser(eq(1L), isA(UserRequestDto.class));
        HttpStatusCode statusCode = actualUpdateUserResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualUpdateUserResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualUpdateUserResult.hasBody());
        HttpHeaders headers = actualUpdateUserResult.getHeaders();
        assertTrue(headers.isEmpty());
        ResponseEntity<List<UserResponseDto>> allUsers = userController.getAllUsers();
        assertEquals(headers, allUsers.getHeaders());
        assertSame(userResponseDto, actualUpdateUserResult.getBody());
        assertSame(statusCode, allUsers.getStatusCode());
    }

    /**
     * Method under test: {@link UserController#updateUser(Long, UserRequestDto)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUser2() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class com.ayd2.librarysystem.user.model.dto.UserRequestDto and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
        //       at com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1308)
        //       at com.fasterxml.jackson.databind.DatabindContext.reportBadDefinition(DatabindContext.java:414)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.failForEmpty(UnknownSerializer.java:53)
        //       at com.fasterxml.jackson.databind.ser.impl.UnknownSerializer.serialize(UnknownSerializer.java:30)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:479)
        //       at com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:318)
        //       at com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4719)
        //       at com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:3964)
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new UserRequestDto("Dr Jane Doe", "janedoe", "jane.doe@example.org",
                        "iloveyou", LocalDate.of(1970, 1, 1), "User Role")));

        // Act
        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }
}
