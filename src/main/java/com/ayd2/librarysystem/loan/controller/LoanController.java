package com.ayd2.librarysystem.loan.controller;


import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.loan.model.dto.LoanRequestDto;
import com.ayd2.librarysystem.loan.model.dto.LoanResponseDto;
import com.ayd2.librarysystem.loan.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/loans")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponseDto> createLoan(@RequestBody @Valid LoanRequestDto loanRequestDto) throws NotFoundException {
        return ResponseEntity.ok(loanService.createLoan(loanRequestDto));
    }
}
