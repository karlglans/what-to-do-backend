package com.karlglans.whattodo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karlglans.whattodo.controllers.vm.TodoVm;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

  @Test
  void getTodos_whenValidToken_shouldGiveOk() throws Exception {
    Mockito.when(userService.getUserId()).thenReturn(existing_user1_id);
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
    Mockito.when(userService.getUserId()).thenReturn(existing_user1_id);
    int someTodoIdByTheUser1 = 100;
    mockMvc.perform(delete(String.format("/api/v1/todos/%d", someTodoIdByTheUser1))
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isOk());
  }

  @Test
  void deleteTodo_whenUserDoNotOwnTheResource_shouldGiveForbidden() throws Exception {
    Mockito.when(userService.getUserId()).thenReturn(existing_user1_id);
    int todoIdBySomeOtherUser = 101;
    mockMvc.perform(delete(String.format("/api/v1/todos/%d", todoIdBySomeOtherUser))
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isForbidden());
  }

  @Test
  void deleteTodo_whenItemIsMissing_shouldGiveOk() throws Exception {
    Mockito.when(userService.getUserId()).thenReturn(existing_user1_id);
    int someMissingTodoId = 1000;
    mockMvc.perform(delete(String.format("/api/v1/todos/%d", someMissingTodoId))
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isNotFound());
  }

//  @Test
//  void updateTodo_whenMissingObject_shouldGiveOk() throws Exception {
//    TodoVm todoVm = new TodoVm(); // view model
//    int someTodoIdOwnedByTheUser1 = 100;
//    mockMvc.perform(patch(String.format("/api/v1/todos/%d", someTodoIdOwnedByTheUser1))
//            .content(objectMapper.writeValueAsString(todoVm))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
//            .andExpect(status().isOk());
//  }

//  @Test
//  void updateTodo_whenUserOwnsTheResourceAndChangingMessageProperty_shouldGiveOk() throws Exception {
//    Mockito.when(userService.getUserId()).thenReturn(existing_user1_id);
//    TodoVm todoVm = new TodoVm(); // view model
//    todoVm.setMessage("changed");
//    int someTodoIdOwnedByTheUser1 = 100;
//    mockMvc.perform(patch(String.format("/api/v1/todos/%d", someTodoIdOwnedByTheUser1))
//            .content(objectMapper.writeValueAsString(todoVm))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
//            .andExpect(status().isOk());
//  }
//
//  @Test
//  void updateTodo_whenUserOwnsTheResourceAndSendingSameMessage_shouldGiveNotModified() throws Exception {
//    Mockito.when(userService.getUserId()).thenReturn(existing_user1_id);
//    TodoVm todoVm = new TodoVm(); // view model
//    String previousMessage = "todo1";
//    todoVm.setMessage(previousMessage);
//    int someTodoIdOwnedByTheUser1 = 100;
//    mockMvc.perform(patch(String.format("/api/v1/todos/%d", someTodoIdOwnedByTheUser1))
//            .content(objectMapper.writeValueAsString(todoVm))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
//            .andExpect(status().isNotModified());
//  }
//
//  @Test
//  void updateTodo_whenUserDoNotOwnTheResource_shouldGiveForbidden() throws Exception {
//    Mockito.when(userService.getUserId()).thenReturn(existing_user1_id);
//    TodoVm todoVm = new TodoVm(); // view model
//    String previousMessage = "todo1";
//    todoVm.setMessage(previousMessage);
//    int todoIdBySomeOtherUser = 101;
//    mockMvc.perform(patch(String.format("/api/v1/todos/%d", todoIdBySomeOtherUser))
//            .content(objectMapper.writeValueAsString(todoVm))
//            .contentType(MediaType.APPLICATION_JSON)
//            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
//            .andExpect(status().isForbidden());
//  }
}
