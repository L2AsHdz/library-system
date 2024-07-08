package com.ayd2.librarysystem.user.service;


import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.career.repository.CareerRepository;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.dto.StudentRequestDto;
import com.ayd2.librarysystem.user.model.dto.StudentResponseDto;
import com.ayd2.librarysystem.user.model.enums.Rol;
import com.ayd2.librarysystem.user.repository.StudentRepository;
import com.ayd2.librarysystem.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CareerRepository careerRepository;

    @InjectMocks
    private StudentService studentService;

    private StudentModel testStudent;
    private StudentRequestDto testStudentRequestDto;
    private StudentResponseDto testStudentResponseDto;

    @BeforeEach
    public void setUp() {
        testStudent = new StudentModel();
        testStudent.setId(1L);
        testStudent.setFullName("Test Student");
        testStudent.setUsername("teststudent");
        testStudent.setPassword("testpassword");
        testStudent.setEmail("student5@email.com");
        testStudent.setBirthDate(LocalDate.parse("2000-01-01"));
        testStudent.setUserRole(Rol.STUDENT);
        testStudent.setStatus((short) 1);
        testStudent.setAcademicNumber(123456L);
        testStudent.setCareerModel(CareerModel.builder().id(1L).name("Career 1").build());

        testStudentRequestDto = new StudentRequestDto(
                testStudent.getFullName(),
                testStudent.getUsername(),
                testStudent.getEmail(),
                testStudent.getPassword(),
                testStudent.getBirthDate(),
                testStudent.getUserRole().name(),
                testStudent.getAcademicNumber(),
                testStudent.getCareerModel().getId()
        );

        testStudentResponseDto = testStudent.toRecord();
    }

    @Test
    public void itShouldGetStudentById() throws NotFoundException {
        when(studentRepository.findById(anyLong())).thenReturn(java.util.Optional.of(testStudent));

        StudentResponseDto studentResponseDto = studentService.getStudentById(1L);

        assertThat(studentResponseDto).isNotNull().isEqualToComparingFieldByField(testStudentResponseDto);

        verify(studentRepository).findById(anyLong());
    }

    @Test
    public void itShouldThrowNotFoundExceptionWhenStudentNotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Student not found");

        verify(studentRepository).findById(anyLong());
    }

    public void itShouldGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(testStudent));

        var studentResponseDtos = studentService.getAllStudents();

        assertThat(studentResponseDtos).isNotNull().hasSize(1).contains(testStudentResponseDto);

        verify(studentRepository).findAll();
    }

    @Test
    public void itShouldFindEmptyListWhenNoStudents() {
        when(studentRepository.findAll()).thenReturn(List.of());

        var studentResponseDtos = studentService.getAllStudents();

        assertThat(studentResponseDtos).isNotNull().isEmpty();

        verify(studentRepository).findAll();
    }

    @Test
    public void itShouldUpdateStudent() throws NotFoundException, DuplicatedEntityException {
        when(studentRepository.findById(anyLong())).thenReturn(java.util.Optional.of(testStudent));
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.empty());
        when(studentRepository.findByAcademicNumber(anyLong())).thenReturn(java.util.Optional.empty());
        when(careerRepository.findById(anyLong())).thenReturn(Optional.of(testStudent.getCareerModel()));
        when(studentRepository.save(testStudent)).thenReturn(testStudent);

        var updatedStudentResponseDto = studentService.updateStudent(testStudent.getId(), testStudentRequestDto);

        assertThat(updatedStudentResponseDto).isNotNull().isEqualToComparingFieldByField(testStudentResponseDto);

        verify(studentRepository).findById(anyLong());
        verify(userRepository).findByEmail(anyString());
        verify(userRepository).findByUsername(anyString());
        verify(studentRepository).findByAcademicNumber(anyLong());
        verify(studentRepository).save(testStudent);
    }

    @Test
    public void itShouldThrowNotFoundException_WhenStudentToUpdateNotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> studentService.updateStudent(1L, testStudentRequestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Student not found");

        verify(studentRepository).findById(anyLong());
    }

    @Test
    public void itShouldThrowDuplicatedEntityException_WhenEmailAlreadyExists() {
        when(studentRepository.findById(anyLong())).thenReturn(java.util.Optional.of(testStudent));
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(testStudent));

        assertThatThrownBy(() -> studentService.updateStudent(1L, testStudentRequestDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("Student with this email already exists");

        verify(studentRepository).findById(anyLong());
        verify(userRepository).findByEmail(anyString());
        verify(userRepository, never()).save(any(StudentModel.class));
    }

    @Test
    public void itShouldThrowDuplicatedEntityException_WhenUsernameAlreadyExists() {
        when(studentRepository.findById(anyLong())).thenReturn(java.util.Optional.of(testStudent));
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(testStudent));

        assertThatThrownBy(() -> studentService.updateStudent(1L, testStudentRequestDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("Student with this username already exists");

        verify(studentRepository).findById(anyLong());
        verify(userRepository).findByEmail(anyString());
        verify(userRepository).findByUsername(anyString());
        verify(userRepository, never()).save(any(StudentModel.class));
    }
}