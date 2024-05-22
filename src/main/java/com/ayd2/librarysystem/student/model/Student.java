package com.ayd2.librarysystem.student.model;

import com.ayd2.librarysystem.career.model.CareerModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id")
    private CareerModel careerModel;

    @Column(name = "academic_number")
    private Long academicNumber;

    @Column(name = "is_sanctioned")
    private Boolean isSanctioned;

}