package com.ayd2.librarysystem.user.model;

import com.ayd2.librarysystem.user.model.dto.UserResponseDto;
import com.ayd2.librarysystem.user.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString @Builder
@Entity
@Table(name = "User")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserModel {
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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Rol userRole;

    @Column(name = "status")
    private Short status = 1;

    public UserResponseDto toRecord(){
        return new UserResponseDto(id, fullName, username, email, password, birthDate, userRole.name(), status == 1);
    }

}