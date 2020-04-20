package com.karlglans.whattodo.services.exceptions;

public class ValidationException extends RuntimeException {
  public ValidationException(String errorMessage) {
    super(errorMessage);
  }
}
