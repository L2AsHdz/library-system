package com.ayd2.librarysystem.user.repository;

import com.ayd2.librarysystem.user.model.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentModel, Long> {
    Optional<StudentModel> findByAcademicNumber(Long academicNumber);
}
