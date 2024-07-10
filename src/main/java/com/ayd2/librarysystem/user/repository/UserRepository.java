package com.ayd2.librarysystem.user.repository;

import com.ayd2.librarysystem.user.model.UserModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    List<UserModel> findAllByUserRoleIn(List<Rol> roles);
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByEmailAndIdNot(String email, Long id);
    Optional<UserModel> findByUsernameAndIdNot(String username, Long id);
}