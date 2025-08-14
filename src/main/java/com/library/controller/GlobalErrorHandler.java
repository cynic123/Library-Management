package com.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
    if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("not found")) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
              .body(Map.of("error", ex.getMessage()));
    }
    return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
  }

  // add other exception handlers here as needed
}