package com.ayd2.librarysystem.book.model.dto;

import com.ayd2.librarysystem.book.model.BookModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.ayd2.librarysystem.book.model.BookModel}
 */
public record BookRequestDto(
		@NotBlank(message = "Title is required")
		@Size(message = "Title must be between 1 and 150 characters", min = 1, max = 150)
		String title,

		@NotBlank(message = "Author is required")
		@Size(message = "Author must be between 1 and 150 characters", min = 1, max = 150)
		String author,

		@NotNull(message = "Publication date is required")
		@Past(message = "Publication date must be in the past")
		LocalDate publicationDate,

		@NotBlank(message = "Publisher is required")
		@Size(message = "Publisher must be between 1 and 150 characters", min = 1, max = 150)
		String publisher
) implements Serializable {
  public BookModel toEntity() {
	return BookModel.builder()
			.title(title)
			.author(author)
			.publicationDate(publicationDate)
			.publisher(publisher)
			.stock(0L)
			.build();
  }
}