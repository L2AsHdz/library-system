package com.ayd2.librarysystem.user.model.dto;

import java.time.LocalDate;

public class StudentResponseDto extends UserResponseDto {

    private final Long academicNumber;
    private final Long careerId;
    private final Boolean isSanctioned;


    public StudentResponseDto(UserResponseDto userResponseDto, Long academicNumber, Long careerId,
                              Boolean isSanctioned) {
        super(userResponseDto.id(), userResponseDto.fullName(), userResponseDto.username(),
                userResponseDto.email(), userResponseDto.password(), userResponseDto.birthDate(),
                userResponseDto.userRole(), userResponseDto.status());
        this.academicNumber = academicNumber;
        this.careerId = careerId;
        this.isSanctioned = isSanctioned;
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

}
