package com.ayd2.librarysystem.user.model.dto;

import lombok.Getter;

@Getter
public class StudentResponseDto extends UserResponseDto {

    private final Long academicNumber;
    private final Long careerId;
    private final Boolean isSanctioned;


    public StudentResponseDto(UserResponseDto userResponseDto, Long academicNumber, Long careerId,
                              Boolean isSanctioned) {
        super(userResponseDto.getId(), userResponseDto.getFullName(), userResponseDto.getUsername(),
                userResponseDto.getEmail(), userResponseDto.getBirthDate(),
                userResponseDto.getUserRole(), userResponseDto.getStatus());
        this.academicNumber = academicNumber;
        this.careerId = careerId;
        this.isSanctioned = isSanctioned;
    }

}
