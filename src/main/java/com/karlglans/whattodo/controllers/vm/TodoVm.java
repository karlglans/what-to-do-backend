package com.karlglans.whattodo.controllers.vm;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoVm {
  private int id;
  private String message;
  private Boolean completed;
}
