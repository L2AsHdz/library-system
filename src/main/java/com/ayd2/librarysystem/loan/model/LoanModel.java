package com.ayd2.librarysystem.loan.model;

import com.ayd2.librarysystem.book.model.BookModel;
import com.ayd2.librarysystem.loan.model.dto.LoanResponseDto;
import com.ayd2.librarysystem.user.model.StudentModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentModel studentModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookModel bookModel;

    @Column(name = "loan_price")
    private Double loanPrice;

    @Column(name = "loan_arrears")
    private Double loanArrears;

    @Column(name = "loan_date")
    private LocalDate loanDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Lob
    @Column(name = "status")
    private String status;

    private LoanResponseDto toResponse() {
        return new LoanResponseDto(
                id,
                studentModel.getId(),
                bookModel.getId(),
                loanDate.toString(),
                returnDate.toString(),
                status
        );
    }

}