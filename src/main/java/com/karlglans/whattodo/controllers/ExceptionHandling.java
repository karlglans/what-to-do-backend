package com.karlglans.whattodo.controllers;

import com.karlglans.whattodo.services.exceptions.IlligalActionException;
import com.karlglans.whattodo.services.exceptions.MissingItemException;
import com.karlglans.whattodo.services.exceptions.MissingUserException;
import com.karlglans.whattodo.services.exceptions.NoChangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @ExceptionHandler(MissingUserException.class)
  protected ResponseEntity<String> handleUnauthorizedException(MissingUserException ex) {
    logger.info(ex.getMessage());
    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(null);
  }

  @ExceptionHandler(MissingItemException.class)
  protected ResponseEntity<String> handleMissingItemException(MissingItemException ex) {
    logger.info(ex.getMessage());
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null);
  }

  @ExceptionHandler(IlligalActionException.class)
  protected ResponseEntity<String> handleIlligalActionException(IlligalActionException ex) {
    logger.info(ex.getMessage());
    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(null);
  }

  @ExceptionHandler(NoChangeException.class)
  protected ResponseEntity<String> handleNoChangeException(NoChangeException ex) {
    logger.info(ex.getMessage());
    return ResponseEntity
            .status(HttpStatus.NOT_MODIFIED)
            .body(null);
  }
}
