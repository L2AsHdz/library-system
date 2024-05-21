package com.ayd2.librarysystem.exception;

public class NotFoundException extends ServiceException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
