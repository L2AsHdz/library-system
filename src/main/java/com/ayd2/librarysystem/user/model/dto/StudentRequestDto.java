package com.ayd2.librarysystem.user.model.dto;

import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class StudentRequestDto extends UserRequestDto {

    @NotNull(message = "The academic number is required")
    private final Long academicNumber;

    @NotNull(message = "The career id is required")
    private final Long careerId;

    @NotNull(message = "The is sanctioned is required")
    private final Boolean isSanctioned = false;

    public StudentRequestDto(String fullName, String username, String email, String password,
                             LocalDate birthDate, String role, Long academicNumber, Long careerId) {
        super(fullName, username, email, password, birthDate, role);
        this.academicNumber = academicNumber;
        this.careerId = careerId;
    }

    public Long academicNumber() {
        return academicNumber;
    }

    public Long careerId() {
        return careerId;
    }

    public Boolean isSanctioned() {
        return isSanctioned;
    }

    public StudentModel toEntity() {
        var student = new StudentModel();
        student.setAcademicNumber(academicNumber);
        student.setCareerModel(CareerModel.builder().id(careerId).build());
        student.setIsSanctioned(isSanctioned);

        student.setFullName(fullName());
        student.setUsername(username());
        student.setEmail(email());
        student.setPassword(password());
        student.setBirthDate(birthDate());
        student.setUserRole(Rol.valueOf(role().toUpperCase()));

        return student;
    }
}
