package com.ayd2.librarysystem.parameter.controller;

import static org.mockito.Mockito.when;

import com.ayd2.librarysystem.parameter.model.dto.ParameterRequestDto;
import com.ayd2.librarysystem.parameter.model.dto.ParameterResponseDto;
import com.ayd2.librarysystem.parameter.service.ParameterService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ParameterController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ParameterControllerDiffblueTest {
    @Autowired
    private ParameterController parameterController;

    @MockBean
    private ParameterService parameterService;

    /**
     * Method under test: {@link ParameterController#getParameters()}
     */
    @Test
    void testGetParameters() throws Exception {
        // Arrange
        when(parameterService.getParameters()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/parameters");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(parameterController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ParameterController#getParameter(Long)}
     */
    @Test
    void testGetParameter() throws Exception {
        // Arrange
        when(parameterService.getParameter(Mockito.<Long>any()))
                .thenReturn(new ParameterResponseDto(1L, "Parameter Name", 10.0d));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/parameters/{id}", 1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(parameterController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"parameterName\":\"Parameter Name\",\"parameterValue\":10.0}"));
    }

    /**
     * Method under test:
     * {@link ParameterController#updateParameter(Long, ParameterRequestDto)}
     */
    @Test
    void testUpdateParameter() throws Exception {
        // Arrange
        when(parameterService.updateParameter(Mockito.<Long>any(), Mockito.<ParameterRequestDto>any()))
                .thenReturn(new ParameterResponseDto(1L, "Parameter Name", 10.0d));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/v1/parameters/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new ParameterRequestDto(1L, 10.0d)));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(parameterController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"parameterName\":\"Parameter Name\",\"parameterValue\":10.0}"));
    }
}
