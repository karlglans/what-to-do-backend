package com.karlglans.whattodo.controllers.vm;

import lombok.Data;

@Data
public class ToggleCompleteResponse {
  private boolean completed;
  public ToggleCompleteResponse(boolean completed) {
    this.completed = completed;
  }
}
