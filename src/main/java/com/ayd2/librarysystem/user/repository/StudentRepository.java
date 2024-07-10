package com.ayd2.librarysystem.user.repository;

import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentModel, Long> {
    Optional<StudentModel> findByAcademicNumber(Long academicNumber);
    Optional<StudentModel> findByAcademicNumberAndIdIsNot(Long academicNumber, Long id);
    List<StudentModel> findAllByUserRoleIs(Rol role);
}
