package com.karlglans.whattodo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karlglans.whattodo.controllers.vm.TodoVm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class TodoControllerPatch extends AbstractMockMvcIT {

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  // valid auth header for token secret: 'aaa', sub: '1'
  private String validAuthHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ" +
          "3aGF0dG9kbyIsInN1YiI6MSwiaWF0IjoxNTg1NDg4MzcxfQ.yFBJrc9h-Hs9q5ns7DKwneLUNBDpMdSIQbTwX-6LepM";


  @Test
  void updateTodo_whenUserOwnsTheResourceAndChangingMessageProperty_shouldGiveOk() throws Exception {

    TodoVm todoVm = new TodoVm(); // view model
    todoVm.setMessage("changed");
    int someTodoIdOwnedByTheUser1 = 100;
    mockMvc.perform(patch(String.format("/api/v1/todos/%d", someTodoIdOwnedByTheUser1))
            .content(objectMapper.writeValueAsString(todoVm))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isOk());
  }

  @Test
  void updateTodo_whenUserOwnsTheResourceAndSendingSameMessage_shouldGiveNotModified() throws Exception {
    TodoVm todoVm = new TodoVm(); // view model
    String previousMessage = "todo1";
    todoVm.setMessage(previousMessage);
    int someTodoIdOwnedByTheUser1 = 100;
    mockMvc.perform(patch(String.format("/api/v1/todos/%d", someTodoIdOwnedByTheUser1))
            .content(objectMapper.writeValueAsString(todoVm))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isNotModified());
  }

  @Test
  void updateTodo_whenUserDoNotOwnTheResource_shouldGiveForbidden() throws Exception {
    TodoVm todoVm = new TodoVm(); // view model
    String previousMessage = "todo1";
    todoVm.setMessage(previousMessage);
    int todoIdBySomeOtherUser = 101;
    mockMvc.perform(patch(String.format("/api/v1/todos/%d", todoIdBySomeOtherUser))
            .content(objectMapper.writeValueAsString(todoVm))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, validAuthHeader))
            .andExpect(status().isForbidden());
  }
}
