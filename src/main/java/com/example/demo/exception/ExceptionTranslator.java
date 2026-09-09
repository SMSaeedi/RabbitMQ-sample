package com.example.demo.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ExceptionTranslator {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public PersistentExceptionResponse handleValidationExceptions(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
            .toList();
        return new PersistentExceptionResponse(Instant.now(), details);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ServiceExceptionResponse handleNotFound(NotFoundException ex) {
        return new ServiceExceptionResponse(Instant.now(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ServiceExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String details = Objects.requireNonNullElse(fieldError != null ? fieldError.getDefaultMessage() : ex.getMessage(), "Validation failed");
        return new ServiceExceptionResponse(Instant.now(), details);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public PersistentExceptionResponse handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new PersistentExceptionResponse(Instant.now(), List.of(ex.getMostSpecificCause().getMessage()));
    }
}
