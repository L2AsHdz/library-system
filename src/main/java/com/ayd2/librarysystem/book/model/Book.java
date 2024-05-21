package com.ayd2.librarysystem.book.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private Long id;

    @Column(name = "title", length = 150)
    private String title;

    @Column(name = "author", length = 150)
    private String author;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "publisher", length = 150)
    private String publisher;

}