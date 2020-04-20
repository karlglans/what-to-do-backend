package com.karlglans.whattodo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karlglans.whattodo.controllers.vm.TodoVm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
class TodoControllerPatch extends AbstractMockMvcIT {

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper = new ObjectMapper();


  @Test
  void updateTodo_whenUserOwnsTheResourceAndChangingMessageProperty_shouldGiveOk() throws Exception {

    TodoVm todoVm = new TodoVm(); // view model
    todoVm.setMessage("changed");
    int someTodoIdOwnedByTheUser1 = 100;
    mockMvc.perform(patch(String.format("/api/v1/todos/%d", someTodoIdOwnedByTheUser1))
            .content(objectMapper.writeValueAsString(todoVm))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, TestDataSetup1.validAuthHeaderUser1))
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
            .header(HttpHeaders.AUTHORIZATION, TestDataSetup1.validAuthHeaderUser1))
            .andExpect(status().isNotModified());
  }

  @Test
  void updateTodo_whenUserDoNotOwnTheResource_shouldGiveForbidden() throws Exception {
    TodoVm todoVm = new TodoVm(); // view model
    String previousMessage = "todo1";
    todoVm.setMessage(previousMessage);
    int todoIdBySomeOtherUser = TestDataSetup1.someTodoIdForTodoByUser2;
    mockMvc.perform(patch(String.format("/api/v1/todos/%d", todoIdBySomeOtherUser))
            .content(objectMapper.writeValueAsString(todoVm))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, TestDataSetup1.validAuthHeaderUser1))
            .andExpect(status().isForbidden());
  }
}
