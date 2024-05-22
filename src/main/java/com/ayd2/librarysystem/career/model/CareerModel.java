package com.ayd2.librarysystem.career.model;

import com.ayd2.librarysystem.career.model.dto.CareerResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Career")
public class CareerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id", nullable = false)
    private Long id;

    @Column(name = "name", length = 150)
    private String name;

    public CareerResponseDto toRecord() {
        return new CareerResponseDto(id, name);
    }

}