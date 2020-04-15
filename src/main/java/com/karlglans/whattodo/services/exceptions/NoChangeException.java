package com.karlglans.whattodo.services.exceptions;

public class NoChangeException extends RuntimeException {
  public NoChangeException(String errorMessage) {
    super(errorMessage);
  }
}