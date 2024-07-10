package com.ayd2.librarysystem.career.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.ayd2.librarysystem.career.model.dto.CareerRequestDto;
import com.ayd2.librarysystem.career.model.dto.CareerResponseDto;
import com.ayd2.librarysystem.career.service.CareerService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

@ContextConfiguration(classes = {CareerController.class})
@ExtendWith(SpringExtension.class)
class CareerControllerTest {
    @Autowired
    private CareerController careerController;

    @MockBean
    private CareerService careerService;

    @Test
    void testGetAllCareers() throws Exception {
        // Arrange
        when(careerService.getAllCareers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/careers");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(careerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    void testGetCareerById() throws Exception {
        // Arrange
        when(careerService.getCareerById(Mockito.<Long>any())).thenReturn(new CareerResponseDto(1L, "Carrera"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/careers/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(careerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Carrera\"}"));
    }

    @Test
    void testCreateCareer() throws Exception {
        // Arrange
        when(careerService.createCareer(Mockito.<CareerRequestDto>any())).thenReturn(new CareerResponseDto(1L, "Carrera"));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/v1/careers")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new CareerRequestDto("Carrera")));

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(careerController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Carrera\"}"));
    }

    @Test
    void testUpdateCareer() throws Exception {
        // Arrange
        when(careerService.updateCareer(Mockito.<Long>any(), Mockito.<CareerRequestDto>any()))
                .thenReturn(new CareerResponseDto(1L, "Carrera2"));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/v1/careers/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new CareerRequestDto("Carrera2")));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(careerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":1,\"name\":\"Carrera2\"}"));
    }

    @Test
    void testDeleteCareer() throws Exception {
        // Arrange
        doNothing().when(careerService).deleteCareer(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/v1/careers/{id}", 1L);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(careerController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted());
    }
}
