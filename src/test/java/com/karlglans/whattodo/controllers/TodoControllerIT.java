package com.karlglans.whattodo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karlglans.whattodo.entities.Todo;
import com.karlglans.whattodo.services.UserService;
import com.karlglans.whattodo.services.exceptions.MissingUserException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class TodoControllerIT extends AbstractMockMvcIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserService userService;

  private ObjectMapper objectMapper = new ObjectMapper();

  private final int existing_user1_id = 1;

  // valid auth header for token secret: 'aaa', sub: '1'
  private String validAuthHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ" +
          "3aGF0dG9kbyIsInN1YiI6MSwiaWF0IjoxNTg1NDg4MzcxfQ.yFBJrc9h-Hs9q5ns7DKwneLUNBDpMdSIQbTwX-6LepM";

  private String invalidAuthHeader = "Bearer invalid";

  @Test
  void getTodos_whenValidToken_shouldGiveOk() throws Exception {
    Mockito.when(userService.getUserId()).thenReturn(existing_user1_id);
    mockMvc.perform(get("/api/v1/todos")
      .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
      .andExpect(status().isOk());
  }

  @Test
  void getTodos_whenValidTokenAndMissingStoredUser_shouldGiveUnauthorized() throws Exception {
    Mockito.when(userService.getUserId()).thenThrow(new MissingUserException("user not found"));
    mockMvc.perform(get("/api/v1/todos")
      .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void getTodos_whenInvalidToken_shouldGiveOk() throws Exception {
    mockMvc.perform(get("/api/v1/todos")
            .header(HttpHeaders.AUTHORIZATION, invalidAuthHeader))
            .andExpect(status().isUnauthorized());
  }

  @Test
  void addTodo_whenInvalidToken_shouldGiveUnauthorized() throws Exception {
    Todo todo = new Todo();
    todo.setMessage("new todo");
    mockMvc.perform(
      post("/api/v1/todos")
        .content(objectMapper.writeValueAsString(todo))
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, invalidAuthHeader))
      .andExpect(status().isUnauthorized());
  }

  @Test
  void addTodo_whenValidToken_shouldGiveOk() throws Exception {
    Todo todo = new Todo();
    todo.setMessage("new todo");
    mockMvc.perform(
      post("/api/v1/todos")
        .content(objectMapper.writeValueAsString(todo))
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
      .andExpect(status().isCreated());
  }

  @Test
  void deleteTodo_whenItemIsMissing_shouldGiveOk() throws Exception {
    int someMissingTodoId = 1000;
    mockMvc.perform(delete(String.format("/api/v1/todos/%d", someMissingTodoId))
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isNotFound());
  }
}
