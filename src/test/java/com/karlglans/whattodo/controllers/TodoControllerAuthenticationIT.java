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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Mainly testing some basic cases when authentication is going wrong
// Will replace UserService with a mock-class found from UserServiceTestConfiguration

@Transactional
@ActiveProfiles("test")
class TodoControllerAuthenticationIT extends AbstractMockMvcIT {

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  private String invalidAuthHeader = "Bearer invalid";

  @Test
  void getTodos_whenInvalidToken_shouldGiveOk() throws Exception {
    mockMvc.perform(get("/api/v1/todos")
            .header(HttpHeaders.AUTHORIZATION, invalidAuthHeader))
            .andExpect(status().isUnauthorized());
  }

  @Test
  void getTodos_whenValidTokenAndMissingStoredUser_shouldGiveUnauthorized() throws Exception {
    UserService localUserServiceMock = Mockito.mock(UserService.class);
    Mockito.when(localUserServiceMock.getUserId()).thenThrow(new MissingUserException("user not found"));
    mockMvc.perform(get("/api/v1/todos")
            .header(HttpHeaders.AUTHORIZATION, TestDataSetup1.validAuthHeaderButMissingUser))
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
}
