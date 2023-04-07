package com.github.cookingideas.application.controller;

import com.github.cookingideas.domain.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleException(ValidationException e, WebRequest request) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}