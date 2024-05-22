package com.ayd2.librarysystem.loan.model;

import com.ayd2.librarysystem.book.model.BookModel;
import com.ayd2.librarysystem.student.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    private Student student;

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

}