package com.ayd2.librarysystem.loan.service;

import com.ayd2.librarysystem.book.model.BookModel;
import com.ayd2.librarysystem.book.repository.BookRepository;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.loan.model.LoanModel;
import com.ayd2.librarysystem.loan.model.dto.LoanRequestDto;
import com.ayd2.librarysystem.loan.model.dto.LoanResponseDto;
import com.ayd2.librarysystem.loan.repository.LoanRepository;
import com.ayd2.librarysystem.user.model.StudentModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    @Transactional
    public LoanResponseDto createLoan(LoanRequestDto loanRequestDto) throws NotFoundException {
        var student = new StudentModel();
        student.setId(loanRequestDto.studentId());

        var loan = LoanModel.builder()
                .studentModel(student)
                .bookModel(BookModel.builder().id(loanRequestDto.bookId()).build())
                .loanPrice(0.0)
                .loanDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(3))
                .status("ACTIVE")
                .build();

        var savedLoan = loanRepository.save(loan);

        var book = bookRepository.findById(loanRequestDto.bookId()).get();

        book.setStock(book.getStock() - 1);
        bookRepository.save(book);

        return loan.toResponse();
    }
}
