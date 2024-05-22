package com.ayd2.librarysystem.parameter.model;

import com.ayd2.librarysystem.parameter.model.dto.ParameterResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Parameter")
public class ParameterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameter_id", nullable = false)
    private Long id;

    @Column(name = "parameter_name", length = 50)
    private String parameterName;

    @Column(name = "parameter_value")
    private Double parameterValue;

    public ParameterResponseDto toRecord() {
        return new ParameterResponseDto(id, parameterName, parameterValue);
    }

}