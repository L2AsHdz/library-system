package com.ayd2.librarysystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class DuplicatedEntityException extends ServiceException {
    private final ProblemDetail detail;

    public DuplicatedEntityException(String message) {
        super(message);

        this.detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        this.detail.setProperty("datetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.detail.setTitle("Duplicated entity");
    }
}
