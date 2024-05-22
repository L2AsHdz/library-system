package com.ayd2.librarysystem.book.model;

import com.ayd2.librarysystem.book.model.dto.BookResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Book")
public class BookModel {
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

    public BookResponseDto toRecord() {
        return new BookResponseDto(this.id, this.title, this.author, this.stock, this.publicationDate, this.publisher);
    }

}