package com.ayd2.librarysystem.loan.model.dto;

public record LoanResponseDto(
        Long id,
        Long studentId,
        Long bookId,
        String loanDate,
        String returnDate,
        String status
) {
}
