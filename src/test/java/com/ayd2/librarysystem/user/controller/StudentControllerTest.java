package com.ayd2.librarysystem.user.controller;

import com.ayd2.librarysystem.AbstractMvcTest;
import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.exception.GlobalExceptionHandler;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.dto.StudentRequestUpdateDto;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.service.StudentService;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {StudentController.class, GlobalExceptionHandler.class})
@ExtendWith(MockitoExtension.class)
public class StudentControllerTest extends AbstractMvcTest {

    @MockBean
    private StudentService studentService;

    private StudentModel testStudent;
    private StudentRequestUpdateDto testStudentRequestUpdateDto;

    @BeforeEach
    public void setUp() {
        testStudent = new StudentModel();
        testStudent.setId(1L);
        testStudent.setFullName("Test Student");
        testStudent.setUsername("teststudent");
        testStudent.setPassword("Student_123");
        testStudent.setEmail("student5@email.com");
        testStudent.setBirthDate(LocalDate.parse("2000-01-01"));
        testStudent.setUserRole(Rol.STUDENT);
        testStudent.setStatus((short) 1);
        testStudent.setAcademicNumber(123456L);
        testStudent.setCareerModel(CareerModel.builder().id(1L).name("Career 1").build());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAllStudents() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(testStudent.toRecord()));

        ResultActions result = mockMvc.perform(get("/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result.andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    String contentAsString = mvcResult.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo(objectMapper.writeValueAsString(List.of(testStudent.toRecord())));
                });
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getStudentById() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(testStudent.toRecord());

        ResultActions result = mockMvc.perform(get("/v1/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        result.andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    String contentAsString = mvcResult.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo(objectMapper.writeValueAsString(testStudent.toRecord()));
                });
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void updateStudent() throws Exception {
        testStudentRequestUpdateDto = new StudentRequestUpdateDto(
                testStudent.getFullName(),
                testStudent.getUsername(),
                testStudent.getEmail(),
                testStudent.getBirthDate(),
                testStudent.getUserRole().name(),
                testStudent.getAcademicNumber(),
                testStudent.getCareerModel().getId()
        );

        when(studentService.updateStudent(1L, testStudentRequestUpdateDto)).thenReturn(testStudent.toRecord());

        ResultActions result = mockMvc.perform(put("/v1/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStudentRequestUpdateDto))
                .with(csrf()));

        result.andExpect(status().isOk());
    }
}