package com.karlglans.whattodo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TodoControllerIT extends AbstractMockMvcIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void canGetResource() throws Exception {
    mockMvc.perform(get("/api/v1/todos"))
            .andExpect(status().isOk());
  }
}
