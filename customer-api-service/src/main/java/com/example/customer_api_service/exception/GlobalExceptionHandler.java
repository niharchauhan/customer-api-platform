package com.example.customer_api_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException ex) {
        logger.warn("Duplicate email error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(" Internal error: " + ex.getMessage());
    }
}
