package com.ayd2.librarysystem.user.service;

import com.ayd2.librarysystem.career.repository.CareerRepository;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.dto.StudentRequestUpdateDto;
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

    public StudentResponseDto getStudentByAcademicNumber(Long academicNumber) throws NotFoundException {
        return studentRepository.findByAcademicNumber(academicNumber)
                .map(StudentModel::toRecord)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentModel::toRecord)
                .toList();
    }

    public StudentResponseDto updateStudent(Long id, StudentRequestUpdateDto studentRequestUpdateDto) throws NotFoundException, DuplicatedEntityException {
        var studentToUpdate = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        var duplicatedByEmail = userRepository.findByEmailAndIdNot(studentRequestUpdateDto.getEmail(), id);
        if (duplicatedByEmail.isPresent())
            throw new DuplicatedEntityException("Student with this email already exists");

        var duplicatedByUsername = userRepository.findByUsernameAndIdNot(studentRequestUpdateDto.getUsername(), id);
        if (duplicatedByUsername.isPresent())
            throw new DuplicatedEntityException("Student with this username already exists");

        var duplicatedByAcademicNumber = studentRepository.findByAcademicNumberAndIdIsNot(studentRequestUpdateDto.getAcademicNumber(), id);
        if (duplicatedByAcademicNumber.isPresent())
            throw new DuplicatedEntityException("Student with this academic number already exists");

        studentToUpdate.setFullName(studentRequestUpdateDto.getFullName());
        studentToUpdate.setUsername(studentRequestUpdateDto.getUsername());
        studentToUpdate.setEmail(studentRequestUpdateDto.getEmail());
        studentToUpdate.setBirthDate(studentRequestUpdateDto.getBirthDate());
        studentToUpdate.setAcademicNumber(studentRequestUpdateDto.getAcademicNumber());
        studentToUpdate.setCareerModel(careerRepository.findById(studentRequestUpdateDto.getCareerId())
                .orElseThrow(() -> new NotFoundException("Career not found")));

        var updatedStudent = studentRepository.save(studentToUpdate);

        return updatedStudent.toRecord();
    }
}
