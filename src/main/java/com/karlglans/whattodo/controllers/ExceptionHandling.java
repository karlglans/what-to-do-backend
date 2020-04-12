package com.karlglans.whattodo.controllers;

import com.karlglans.whattodo.services.exceptions.MissingUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling {

  @ExceptionHandler(MissingUserException.class)
  protected ResponseEntity<String> handleUnauthorizedException(MissingUserException ex) {
    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(null);
  }
}
