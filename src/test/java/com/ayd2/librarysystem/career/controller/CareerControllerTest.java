package com.ayd2.librarysystem.career.controller;

import com.ayd2.librarysystem.AbstractMvcTest;
import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.career.model.dto.CareerRequestDto;
import com.ayd2.librarysystem.career.model.dto.CareerResponseDto;
import com.ayd2.librarysystem.career.service.CareerService;
import com.ayd2.librarysystem.exception.GlobalExceptionHandler;
import com.ayd2.librarysystem.user.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CareerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {CareerController.class, GlobalExceptionHandler.class})
@ExtendWith(MockitoExtension.class)
class CareerControllerTest extends AbstractMvcTest {

    @MockBean
    private CareerService careerService;

    private CareerModel testCareer;
    private CareerRequestDto testCareerRequestDto;
    private CareerResponseDto testCareerResponseDto;

    @BeforeEach
    public void setUp() {
        testCareer = CareerModel.builder()
                .id(1L)
                .name("Test Career")
                .build();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllCareers() throws Exception {
        when(careerService.getAllCareers()).thenReturn(List.of(testCareer.toRecord()));

        ResultActions result = mockMvc.perform(get("/v1/careers")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result.andExpect(status().isOk())
                .andExpect((r) -> {
                    String response = r.getResponse().getContentAsString();
                    assertThat(response).isEqualTo(objectMapper.writeValueAsString(List.of(testCareer.toRecord())));
                });
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getCareerById() throws Exception {
        when(careerService.getCareerById(1L)).thenReturn(testCareer.toRecord());

        ResultActions result = mockMvc.perform(get("/v1/careers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result.andExpect(status().isOk())
                .andExpect((r) -> {
                    String response = r.getResponse().getContentAsString();
                    assertThat(response).isEqualTo(objectMapper.writeValueAsString(testCareer.toRecord()));
                });
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void createCareer() throws Exception {
        createCareerRequestDto();

        when(careerService.createCareer(testCareerRequestDto)).thenReturn(testCareer.toRecord());

        ResultActions result = mockMvc.perform(post("/v1/careers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCareerRequestDto))
                .with(csrf()));

        result.andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateCareer() throws Exception {
        createCareerRequestDto();

        when(careerService.updateCareer(1L, testCareerRequestDto)).thenReturn(testCareer.toRecord());

        ResultActions result = mockMvc.perform(put("/v1/careers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCareerRequestDto))
                .with(csrf()));

        result.andExpect(status().isOk());
    }

    private void createCareerRequestDto() {
        testCareerRequestDto = new CareerRequestDto(
                testCareer.getName()
        );
    }
}