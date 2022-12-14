package com.example.demo.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class ExceptionTranslator {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public PersistentExceptionResponse handleValidationExceptions(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations()
                .parallelStream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        return PersistentExceptionResponse.builder()
                .timestamp(new Date())
                .details(details)
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ServiceExceptionResponse handleValidationExceptions(NotFoundException ex) {
        return ServiceExceptionResponse.builder()
                .timestamp(new Date())
                .details(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ServiceExceptionResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ServiceExceptionResponse.builder()
                .timestamp(new Date())
                .details(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public PersistentExceptionResponse handleValidationExceptions(DataIntegrityViolationException ex) {
        List<String> strLst = new ArrayList<>();
        strLst.add(ex.getMessage());
        return PersistentExceptionResponse.builder()
                .timestamp(new Date())
                .details(strLst)
                .build();
    }
}