package com.karlglans.whattodo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karlglans.whattodo.entities.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
class TodoControllerIT extends AbstractMockMvcIT {

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  // valid auth header for token secret: 'aaa', sub: '1'
  private String validAuthHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ" +
          "3aGF0dG9kbyIsInN1YiI6MSwiaWF0IjoxNTg1NDg4MzcxfQ.yFBJrc9h-Hs9q5ns7DKwneLUNBDpMdSIQbTwX-6LepM";

  @Test
  void getTodos_whenValidToken_shouldGiveOk() throws Exception {
    mockMvc.perform(get("/api/v1/todos")
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isOk());
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
  void deleteTodo_whenUserOwnsTheResource_shouldGiveOk() throws Exception {
    int someTodoIdByTheUser1 = 100;
    mockMvc.perform(delete(String.format("/api/v1/todos/%d", someTodoIdByTheUser1))
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isOk());
  }

  @Test
  void deleteTodo_whenUserDoNotOwnTheResource_shouldGiveForbidden() throws Exception {
    int todoIdBySomeOtherUser = 106;
    mockMvc.perform(delete(String.format("/api/v1/todos/%d", todoIdBySomeOtherUser))
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isForbidden());
  }

  @Test
  void deleteTodo_whenItemIsMissing_shouldGiveOk() throws Exception {
    int someMissingTodoId = 1000;
    mockMvc.perform(delete(String.format("/api/v1/todos/%d", someMissingTodoId))
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isNotFound());
  }

  @Test
  void toggleComplete_whenIncompleteTodos_shouldGiveOk() throws Exception {
    mockMvc.perform(
            post("/api/v1/todos/toggle-complete")
                    .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isOk());
  }

  @Test
  void deleteCompleted_whenCompletedTodosExists_shouldGiveOk() throws Exception {
    // Precondition: User 1 has some completed todos
    mockMvc.perform(
            delete("/api/v1/todos/delete-completed")
                    .header(HttpHeaders.AUTHORIZATION, TestDataSetup1.validAuthHeaderUser1))
            .andExpect(status().isOk());
  }

  @Test
  void deleteCompleted_whenCompletedTodosDoNotExists_shouldGiveNotModified() throws Exception {
    // Precondition: User 2 has no completed todos
    mockMvc.perform(
            delete("/api/v1/todos/delete-completed")
                    .header(HttpHeaders.AUTHORIZATION, TestDataSetup1.validAuthHeaderUser2))
            .andExpect(status().isNotModified());
  }

}
