package com.ayd2.librarysystem.user.model;

import com.ayd2.librarysystem.career.model.CareerModel;
import com.ayd2.librarysystem.user.model.dto.StudentResponseDto;
import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Student")
public class StudentModel extends UserModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id")
    private CareerModel careerModel;

    @Column(name = "academic_number")
    private Long academicNumber;

    @Column(name = "is_sanctioned")
    private Boolean isSanctioned;

    public StudentResponseDto toRecord() {
        return new StudentResponseDto(
                super.toRecord(),
                academicNumber,
                careerModel.getId(),
                isSanctioned
        );
    }

}