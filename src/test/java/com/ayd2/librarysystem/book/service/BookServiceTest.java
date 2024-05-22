package com.ayd2.librarysystem.book.service;

import com.ayd2.librarysystem.book.model.BookModel;
import com.ayd2.librarysystem.book.model.dto.BookRequestDto;
import com.ayd2.librarysystem.book.model.dto.BookResponseDto;
import com.ayd2.librarysystem.book.repository.BookRepository;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    private BookModel testBook;
    private BookRequestDto testBookRequestDto;
    private BookResponseDto testBookResponseDto;

    @BeforeEach
    public void setUp() {
        testBook = BookModel.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .publicationDate(LocalDate.parse("2021-01-01"))
                .publisher("Test Publisher")
                .stock(10L)
                .build();

        testBookRequestDto = new BookRequestDto(
                testBook.getTitle(),
                testBook.getAuthor(),
                testBook.getPublicationDate(),
                testBook.getPublisher()
        );

        testBookResponseDto = testBook.toRecord();
    }

    @Test
    public void itShouldFindBookById() throws NotFoundException {
        // Arrange
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(testBook));

        // Act
        var bookExpected = bookService.getBookById(testBook.getId());

        // Assert
        assertThat(bookExpected).isNotNull().isEqualToComparingFieldByField(testBookResponseDto);
        verify(bookRepository).findById(testBook.getId());
    }

    @Test
    public void itShouldThrowException_WhenBookDoesntExists() {
        // Arrange
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> bookService.getBookById(testBook.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book not found");
        verify(bookRepository).findById(testBook.getId());
    }

    @Test
    public void itShouldFindAllBooks() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of(testBook));

        // Act
        var books = bookService.getAllBooks();

        // Assert
        assertThat(books).isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(testBookResponseDto);
        verify(bookRepository).findAll();
    }

    @Test
    public void itShouldFindEmptyList_WhenNoBooks() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(java.util.List.of());

        // Act
        var books = bookService.getAllBooks();

        // Assert
        assertThat(books).isNotNull()
                .isEmpty();
        verify(bookRepository).findAll();
    }

    @Test
    public void itShouldCreateNewBook() throws DuplicatedEntityException {
        // Arrange
        when(bookRepository.findByTitle(any(String.class))).thenReturn(Optional.empty());
        when(bookRepository.save(any(BookModel.class))).thenReturn(testBook);

        // Act
        var bookExpected = bookService.createBook(testBookRequestDto);

        // Assert
        assertThat(bookExpected).isNotNull().isEqualToComparingFieldByField(testBookResponseDto);
        verify(bookRepository).findByTitle(testBook.getTitle());
        verify(bookRepository).save(any(BookModel.class));
    }

    @Test
    public void itShouldThrowException_WhenBookAlreadyExists() {
        // Arrange
        when(bookRepository.findByTitle(any(String.class))).thenReturn(Optional.of(testBook));

        // Act and Assert
        assertThatThrownBy(() -> bookService.createBook(testBookRequestDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("Book already exists");
        verify(bookRepository).findByTitle(testBook.getTitle());
    }

    @Test
    public void itShouldUpdateBook() throws ServiceException {
        // Arrange
        when(bookRepository.findByTitle(any(String.class))).thenReturn(Optional.empty());
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(BookModel.class))).thenReturn(testBook);

        // Act
        var bookExpected = bookService.updateBook(testBook.getId(), testBookRequestDto);

        // Assert
        assertThat(bookExpected).isNotNull().isEqualToComparingFieldByField(testBookResponseDto);
        verify(bookRepository).findById(testBook.getId());
        verify(bookRepository).save(testBook);
        verify(bookRepository).findByTitle(testBook.getTitle());
    }

    @Test
    public void itShouldThrowException_WhenBookToUpdateDoesntExists() {
        // Arrange
        when(bookRepository.findByTitle(any(String.class))).thenReturn(Optional.empty());
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> bookService.updateBook(testBook.getId(), testBookRequestDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book not found");
        verify(bookRepository).findById(testBook.getId());
        verify(bookRepository).findByTitle(testBook.getTitle());
        verify(bookRepository, never()).save(any(BookModel.class));
    }

    @Test
    public void itShouldThrowException_WhenBookAlreadyExistsOnUpdate() {
        // Arrange
        when(bookRepository.findByTitle(any(String.class))).thenReturn(Optional.of(testBook));

        // Act and Assert
        assertThatThrownBy(() -> bookService.updateBook(testBook.getId(), testBookRequestDto))
                .isInstanceOf(DuplicatedEntityException.class)
                .hasMessage("Book with this title already exists");
        verify(bookRepository).findByTitle(testBook.getTitle());
        verify(bookRepository, never()).findById(testBook.getId());
        verify(bookRepository, never()).save(any(BookModel.class));
    }

    @Test
    public void itShouldDeleteBook() throws NotFoundException {
        // Arrange
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(testBook));

        // Act
        bookService.deleteBook(testBook.getId());

        // Assert
        verify(bookRepository).findById(testBook.getId());
        verify(bookRepository).delete(testBook);
    }

    @Test
    public void itShouldThrowException_WhenBookToDeleteDoesntExists() {
        // Arrange
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act and Assert
        assertThatThrownBy(() -> bookService.deleteBook(testBook.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book not found");
        verify(bookRepository).findById(testBook.getId());
        verify(bookRepository, never()).delete(any(BookModel.class));
    }

    @Test
    public void itShouldFindBooksByStockLessThan() {
        // Arrange
        when(bookRepository.findAllByStockLessThan(any(Long.class))).thenReturn(java.util.List.of(testBook));

        // Act
        var books = bookService.getBooksByStockLessThan(20L);

        // Assert
        assertThat(books).isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(testBookResponseDto);
        verify(bookRepository).findAllByStockLessThan(20L);
    }

    @Test
    public void itShouldFindEmptyList_WhenNoBooksByStockLessThan() {
        // Arrange
        when(bookRepository.findAllByStockLessThan(any(Long.class))).thenReturn(java.util.List.of());

        // Act
        var books = bookService.getBooksByStockLessThan(5L);

        // Assert
        assertThat(books).isNotNull()
                .isEmpty();
        verify(bookRepository).findAllByStockLessThan(5L);
    }

}