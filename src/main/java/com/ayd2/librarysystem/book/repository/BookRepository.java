package com.ayd2.librarysystem.book.repository;

import com.ayd2.librarysystem.book.model.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookModel, Long> {
    Optional<BookModel> findByTitle(String title);
    Optional<BookModel> findByTitleAndIdNot(String title, Long id);
    List<BookModel> findAllByStockLessThan(Long stock);
}