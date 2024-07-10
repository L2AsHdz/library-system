package com.ayd2.librarysystem.user.controller;

import static org.mockito.Mockito.when;

import com.ayd2.librarysystem.user.model.dto.StudentRequestDto;
import com.ayd2.librarysystem.user.model.dto.StudentResponseDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import com.ayd2.librarysystem.user.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {StudentController.class})
@ExtendWith(SpringExtension.class)
class StudentControllerDiffblueTest {
    @Autowired
    private StudentController studentController;

    @MockBean
    private StudentService studentService;

    @Test
    void testGetAllStudents() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/students");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(studentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetStudentById() throws Exception {
        // Arrange
        when(studentService.getStudentById(Mockito.<Long>any()))
                .thenReturn(new StudentResponseDto(new UserResponseDto(1L, "Leonidas", "leonidas", "leonidas@example.org",
                        "hola123", LocalDate.of(1970, 1, 1), "STUDENT", true), 1L, 1L, true));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/students/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(studentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"fullName\":\"Leonidas\",\"username\":\"leonidas\",\"email\":\"leonidas@example.org\",\"password\":"
                                        + "\"hola123\",\"birthDate\":[1970,1,1],\"userRole\":\"STUDENT\",\"status\":true,\"academicNumber\":1,\"careerId\""
                                        + ":1,\"isSanctioned\":true}"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        // Arrange
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/v1/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new StudentRequestDto("Leonidas", "leonidas", "leonidas@example.org",
                        "hola123", LocalDate.of(1970, 1, 1), "STUDENT", 1L, 1L)));

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(studentController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}
