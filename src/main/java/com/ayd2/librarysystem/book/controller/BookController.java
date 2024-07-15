package com.ayd2.librarysystem.book.controller;

import com.ayd2.librarysystem.book.model.dto.BookRequestDto;
import com.ayd2.librarysystem.book.model.dto.BookResponseDto;
import com.ayd2.librarysystem.book.service.BookService;
import com.ayd2.librarysystem.exception.DuplicatedEntityException;
import com.ayd2.librarysystem.exception.NotFoundException;
import com.ayd2.librarysystem.exception.ServiceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
@PreAuthorize("hasAuthority('ADMIN')")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@RequestBody @Valid BookRequestDto bookRequestDto) throws DuplicatedEntityException {
        return ResponseEntity.created(null).body(bookService.createBook(bookRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @RequestBody @Valid BookRequestDto bookRequestDto) throws ServiceException {
        return ResponseEntity.ok(bookService.updateBook(id, bookRequestDto));
    }

    @PutMapping("/stock/{id}/{quantity}")
    public ResponseEntity<BookResponseDto> updateStock(@PathVariable Long id, @PathVariable Long quantity) throws ServiceException {
        return ResponseEntity.ok(bookService.updateStock(id, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) throws NotFoundException {
        bookService.deleteBook(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<BookResponseDto>> getBooksByStockLessThan() {
        return ResponseEntity.ok(bookService.getBooksByStockLessThan(1L));
    }

}
