package com.ayd2.librarysystem.book.model.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.ayd2.librarysystem.book.model.BookModel}
 */
public record BookResponseDto(Long id, String title, String author, Long stock, LocalDate publicationDate,
                              String publisher) implements Serializable {
}