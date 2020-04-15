package com.karlglans.whattodo.services.exceptions;

public class IlligalActionException extends RuntimeException {
  public IlligalActionException(String errorMessage) {
    super(errorMessage);
  }
}
