package com.ayd2.librarysystem.parameter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Parameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameter_id", nullable = false)
    private Long id;

    @Column(name = "parameter_name", length = 50)
    private String parameterName;

    @Column(name = "parameter_value")
    private Double parameterValue;

}