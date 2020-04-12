package com.karlglans.whattodo.services.exceptions;

public class MissingUserException extends RuntimeException {
  public MissingUserException(String errorMessage) {
    super(errorMessage);
  }
}

