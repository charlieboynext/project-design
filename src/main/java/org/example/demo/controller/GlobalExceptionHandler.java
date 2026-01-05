package org.example.demo.controller;

import org.example.demo.model.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CommonResponse> handleValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(err -> err.getField() + " " + err.getDefaultMessage())
        .orElse("Validation error");
    return new ResponseEntity<>(new CommonResponse("VALIDATION_ERROR", message, null), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<CommonResponse> handleIllegalArgument(IllegalArgumentException ex) {
    return new ResponseEntity<>(new CommonResponse("BAD_REQUEST", ex.getMessage(), null), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CommonResponse> handleGeneric(Exception ex) {
    return new ResponseEntity<>(new CommonResponse("SERVER_ERROR", ex.getMessage(), null),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
