package com.ayd2.librarysystem.user.service;

import com.ayd2.librarysystem.career.repository.CareerRepository;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.dto.StudentRequestDto;
import com.ayd2.librarysystem.user.model.dto.StudentResponseDto;
import com.ayd2.librarysystem.user.repository.StudentRepository;
import com.ayd2.librarysystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final CareerRepository careerRepository;

    public StudentResponseDto getStudentById(Long id) throws NotFoundException {
        return studentRepository.findById(id)
                .map(StudentModel::toRecord)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentModel::toRecord)
                .toList();
    }

    public StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto) throws NotFoundException, DuplicatedEntityException {
        var studentToUpdate = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        var duplicatedByEmail = userRepository.findByEmailAndIdNot(studentRequestDto.email(), id);
        if (duplicatedByEmail.isPresent())
            throw new DuplicatedEntityException("Student with this email already exists");

        var duplicatedByUsername = userRepository.findByUsernameAndIdNot(studentRequestDto.username(), id);
        if (duplicatedByUsername.isPresent())
            throw new DuplicatedEntityException("Student with this username already exists");

        var duplicatedByAcademicNumber = studentRepository.findByAcademicNumberAndIdIsNot(studentRequestDto.academicNumber(), id);
        if (duplicatedByAcademicNumber.isPresent())
            throw new DuplicatedEntityException("Student with this academic number already exists");

        studentToUpdate.setFullName(studentRequestDto.fullName());
        studentToUpdate.setUsername(studentRequestDto.username());
        studentToUpdate.setPassword(studentRequestDto.password());
        studentToUpdate.setEmail(studentRequestDto.email());
        studentToUpdate.setBirthDate(studentRequestDto.birthDate());
        studentToUpdate.setAcademicNumber(studentRequestDto.academicNumber());
        studentToUpdate.setCareerModel(careerRepository.findById(studentRequestDto.careerId())
                .orElseThrow(() -> new NotFoundException("Career not found")));

        var updatedStudent = studentRepository.save(studentToUpdate);

        return updatedStudent.toRecord();
    }
}
