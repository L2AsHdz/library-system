package com.ayd2.librarysystem.loan.service;

import com.ayd2.librarysystem.loan.model.LoanModel;
import com.ayd2.librarysystem.loan.repository.LoanRepository;
import com.ayd2.librarysystem.user.repository.StudentRepository;
import com.ayd2.librarysystem.user.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatesService {

    private final LoanRepository loanRepository;
    private final StudentRepository studentRepository;

    public void priceCalculation() {
        var loans = loanRepository.findAllByStatus("ACTIVE");

        loans.forEach(loan -> {
            if (loan.getReturnDate().isAfter(LocalDate.now())){
                loan.setStatus("ARREARS");
                loan.setLoanArrears(loan.getLoanArrears() + 15);
            } else {
                loan.setLoanPrice(loan.getLoanPrice() + 5);
            }
        });
    }

    public void arrearCalculation() {
        var loans = loanRepository.findAllByStatus("ARREARS");

        loans.forEach(loan -> {
            var datePlusOneMonth = loan.getReturnDate().plusMonths(1);
            var dateNow = LocalDate.now();
            if (datePlusOneMonth.isBefore(dateNow) || datePlusOneMonth.isEqual(dateNow)) {
                loan.setStatus("EXPIRED");
                var student = studentRepository.findById(loan.getStudentModel().getId()).get();
                student.setStatus((short)0);
            } else {
                loan.setLoanArrears(loan.getLoanArrears() + 15);
            }

        });
    }
}
