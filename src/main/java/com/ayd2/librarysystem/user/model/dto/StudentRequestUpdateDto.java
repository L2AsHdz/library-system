package com.ayd2.librarysystem.user.model.dto;

import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StudentRequestUpdateDto extends UserRequestUpdateDto {

    @NotNull(message = "The academic number is required")
    private final Long academicNumber;

    @NotNull(message = "The career id is required")
    private final Long careerId;

    @NotNull(message = "The is sanctioned is required")
    private final Boolean isSanctioned = false;

    public StudentRequestUpdateDto(String fullName, String username, String email,
                                   LocalDate birthDate, String role, Long academicNumber, Long careerId) {
        super(fullName, username, email, birthDate, role);
        this.academicNumber = academicNumber;
        this.careerId = careerId;
    }

    public Boolean isSanctioned() {
        return isSanctioned;
    }

    public StudentModel toEntity() {
        var student = new StudentModel();
        student.setAcademicNumber(academicNumber);
        student.setCareerModel(CareerModel.builder().id(careerId).build());
        student.setIsSanctioned(isSanctioned);
        student.setFullName(getFullName());
        student.setUsername(getUsername());
        student.setEmail(getEmail());
        student.setBirthDate(getBirthDate());
        student.setUserRole(Rol.STUDENT);

        return student;
    }
}
