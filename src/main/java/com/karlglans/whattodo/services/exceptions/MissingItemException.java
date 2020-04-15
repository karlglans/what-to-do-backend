package com.karlglans.whattodo.services.exceptions;

public class MissingItemException extends RuntimeException {
  public MissingItemException(String errorMessage) {
    super(errorMessage);
  }
}
