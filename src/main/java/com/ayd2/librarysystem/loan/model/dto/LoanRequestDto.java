package com.ayd2.librarysystem.loan.model.dto;

public record LoanRequestDto(
        Long studentId,
        Long bookId
) {
}
