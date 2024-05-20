package com.ayd2.librarysystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 500)
    private String password;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Lob
    @Column(name = "user_role")
    private String userRole;

}