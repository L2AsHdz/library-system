package com.ayd2.librarysystem.loan.repository;

import com.ayd2.librarysystem.loan.model.LoanModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<LoanModel, Long> {
    List<LoanModel> findAllByStatus(String status);
}