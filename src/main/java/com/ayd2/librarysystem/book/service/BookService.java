package com.ayd2.librarysystem.book.service;

import com.ayd2.librarysystem.book.model.BookModel;
import com.ayd2.librarysystem.book.model.dto.BookRequestDto;
import com.ayd2.librarysystem.book.model.dto.BookResponseDto;
import com.ayd2.librarysystem.book.repository.BookRepository;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponseDto getBookById(Long id) throws NotFoundException {
        return bookRepository.findById(id)
                .map(BookModel::toRecord)
                .orElseThrow(() -> new NotFoundException("Book not found"));
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(BookModel::toRecord)
                .toList();
    }

    public BookResponseDto createBook(BookRequestDto bookRequestDto) throws DuplicatedEntityException {
        var duplicatedByTitle = bookRepository.findByTitle(bookRequestDto.title());
        if (duplicatedByTitle.isPresent())
            throw new DuplicatedEntityException("Book already exists");

        var newBook = bookRepository.save(bookRequestDto.toEntity());

        return newBook.toRecord();
    }

    public BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto) throws ServiceException {
        var bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        var duplicatedByTitle = bookRepository.findByTitleAndIdNot(bookRequestDto.title(), id);
        if (duplicatedByTitle.isPresent())
            throw new DuplicatedEntityException("Book with this title already exists");

        bookToUpdate.setTitle(bookRequestDto.title());
        bookToUpdate.setAuthor(bookRequestDto.author());
        bookToUpdate.setPublicationDate(bookRequestDto.publicationDate());
        bookToUpdate.setPublisher(bookRequestDto.publisher());

        var updatedBook = bookRepository.save(bookToUpdate);

        return updatedBook.toRecord();
    }

    public BookResponseDto updateStock(Long id, Long quantity) throws ServiceException {
        var bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        bookToUpdate.setStock(quantity);

        var updatedBook = bookRepository.save(bookToUpdate);

        return updatedBook.toRecord();
    }

    public void deleteBook(Long id) throws NotFoundException {
        var bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        bookRepository.delete(bookToDelete);
    }

    public List<BookResponseDto> getBooksByStockLessThan(Long stock) {
        return bookRepository.findAllByStockLessThan(stock).stream()
                .map(BookModel::toRecord)
                .toList();
    }
}
