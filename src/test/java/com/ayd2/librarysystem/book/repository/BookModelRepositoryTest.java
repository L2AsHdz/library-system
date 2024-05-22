package com.ayd2.librarysystem.book.repository;

import com.ayd2.librarysystem.book.model.BookModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
class BookModelRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private BookModel testBook;

    @BeforeEach
    public void setUp() {
        testBook = BookModel.builder()
                .title("Test Book")
                .author("Test Author")
                .publicationDate(LocalDate.parse("2021-01-01"))
                .publisher("Test Publisher")
                .stock(10L)
                .build();
    }

    @Test
    public void itShouldFindBookByTitle() {
        // Arrange
        bookRepository.save(testBook);

        // Act
        var oResult = bookRepository.findByTitle(testBook.getTitle());
        var result = oResult.get();

        // Assert
        assertThat(oResult).isPresent();
        assertThat(result).isNotNull().isEqualToComparingFieldByField(testBook);
    }

    @Test
    public void itShouldFindAllBooksByStockLessThan() {
        // Arrange
        bookRepository.save(testBook);

        // Act
        var result = bookRepository.findAllByStockLessThan(20L);

        // Assert
        assertThat(result).isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(testBook);
    }

    @Test
    public void itShouldNotFindBookByTitle() {
        // Arrange
        bookRepository.save(testBook);

        // Act
        var oResult = bookRepository.findByTitle("Nonexistent Book");

        // Assert
        assertThat(oResult).isEmpty();
    }

    @Test
    public void itShouldNotFindAllBooksByStockLessThan() {
        // Arrange
        bookRepository.save(testBook);

        // Act
        var result = bookRepository.findAllByStockLessThan(5L);

        // Assert
        assertThat(result).isNotNull()
                .isEmpty();
    }

}