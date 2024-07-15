package com.ayd2.librarysystem.user.controller;

import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.user.model.dto.StudentRequestCreateDto;
import com.ayd2.librarysystem.user.model.dto.StudentRequestUpdateDto;
import com.ayd2.librarysystem.user.model.dto.StudentResponseDto;
import com.ayd2.librarysystem.user.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/academic-number/{id}")
    public ResponseEntity<StudentResponseDto> getStudentByAcademicNumber(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(studentService.getStudentByAcademicNumber(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id, @RequestBody @Valid StudentRequestUpdateDto studentRequestUpdateDto) throws NotFoundException, DuplicatedEntityException {
        return ResponseEntity.ok(studentService.updateStudent(id, studentRequestUpdateDto));
    }
}
