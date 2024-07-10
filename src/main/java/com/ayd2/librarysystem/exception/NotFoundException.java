package com.ayd2.librarysystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class NotFoundException extends ServiceException {
    private final ProblemDetail detail;

    public NotFoundException(String message) {
        super(message);

        this.detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        this.detail.setProperty("datetime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.detail.setTitle("Not Found");
    }
}
