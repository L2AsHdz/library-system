package com.ayd2.librarysystem.career.repository;

import com.ayd2.librarysystem.career.model.CareerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CareerRepository extends JpaRepository<CareerModel, Long> {
  Optional<CareerModel> findByName(String name);
}