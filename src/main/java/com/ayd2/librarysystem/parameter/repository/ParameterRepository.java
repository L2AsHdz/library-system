package com.ayd2.librarysystem.parameter.repository;

import com.ayd2.librarysystem.parameter.model.ParameterModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<ParameterModel, Long> {
}