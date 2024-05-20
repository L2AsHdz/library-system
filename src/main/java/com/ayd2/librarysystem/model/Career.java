package com.ayd2.librarysystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id", nullable = false)
    private Long id;

    @Column(name = "name", length = 150)
    private String name;

}