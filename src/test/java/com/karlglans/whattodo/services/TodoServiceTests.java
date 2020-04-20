package com.karlglans.whattodo.services;

import com.karlglans.whattodo.controllers.vm.TodoVm;
import com.karlglans.whattodo.entities.Todo;
import com.karlglans.whattodo.entities.User;
import com.karlglans.whattodo.repositories.TodoRepository;
import com.karlglans.whattodo.services.exceptions.NoChangeException;
import com.karlglans.whattodo.services.exceptions.ValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TodoServiceTests {

  @MockBean
  TodoRepository todoRepositoryMock;

  @MockBean
  UserService userServiceMock;

  @Autowired
  TodoService todoService;

  @Test
  public void toggleAllTodosCompleteStatus_whenMixOfIncompleteAndCompete_shouldGiveTrue() {
    List<Todo> todos = new ArrayList<>();
    todos.add( Todo.builder().completed(false).build());
    todos.add( Todo.builder().completed(true).build());
    when(userServiceMock.getUserId()).thenReturn(1);
    when(todoRepositoryMock.findAllByUserId(1)).thenReturn(todos);
    when(todoRepositoryMock.saveAll(todos)).thenReturn(null);
    boolean completeStatusForAllTodos = todoService.toggleAllTodosCompleteStatus();
    Assert.assertTrue(completeStatusForAllTodos);
  }

  @Test
  public void toggleAllTodosCompleteStatus_whenJustIncompleteTodos_shouldGiveTrue() {
    List<Todo> todos = new ArrayList<>();
    todos.add( Todo.builder().completed(false).build());
    todos.add( Todo.builder().completed(false).build());
    when(userServiceMock.getUserId()).thenReturn(1);
    when(todoRepositoryMock.findAllByUserId(1)).thenReturn(todos);
    when(todoRepositoryMock.saveAll(todos)).thenReturn(null);
    boolean completeStatusForAllTodos = todoService.toggleAllTodosCompleteStatus();
    Assert.assertTrue(completeStatusForAllTodos);
  }

  @Test
  public void toggleAllTodosCompleteStatus_whenJustCompleteTodos_shouldGiveFalse() {
    List<Todo> todos = new ArrayList<>();
    todos.add( Todo.builder().completed(true).build());
    todos.add( Todo.builder().completed(true).build());
    when(userServiceMock.getUserId()).thenReturn(1);
    when(todoRepositoryMock.findAllByUserId(1)).thenReturn(todos);
    when(todoRepositoryMock.saveAll(todos)).thenReturn(null);
    boolean completeStatusForAllTodos = todoService.toggleAllTodosCompleteStatus();
    Assert.assertFalse(completeStatusForAllTodos);
  }

  @Test
  public void toggleAllTodosCompleteStatus_whenNoTodos_shouldGiveFalse() {
    List<Todo> todos = new ArrayList<>();
    when(userServiceMock.getUserId()).thenReturn(1);
    when(todoRepositoryMock.findAllByUserId(1)).thenReturn(todos);
    when(todoRepositoryMock.saveAll(todos)).thenReturn(null);
    Exception exception = assertThrows(NoChangeException.class, () -> {
      todoService.toggleAllTodosCompleteStatus();
    });
    Assert.assertTrue(exception.getMessage().contains("no todos found"));
  }

  @Test
  public void alterTodo_whenParameterTodoHasOnlyPropertyCompleted_shouldOnlyAlterCompleted() {
    final int someTodoId = 10;
    final boolean changedTodoCompeteStatus = true, originalTodoCompeteStatus = false;
    TodoVm todoVm = TodoVm.builder().message(null).completed(changedTodoCompeteStatus).build();

    String originalTodoMessage = "original message";
    User storedUser = User.builder().id(1).build();
    Todo storedTodo = Todo.builder().message(originalTodoMessage)
            .completed(originalTodoCompeteStatus).user(storedUser).build();

    when(userServiceMock.getUserId()).thenReturn(storedUser.getId());
    when(todoRepositoryMock.findById(someTodoId)).thenReturn(Optional.of(storedTodo)) ;
    when(todoRepositoryMock.save(storedTodo)).thenReturn(null); // return is ignored

    // act
    todoService.alterTodo(todoVm, someTodoId);

    // should have changed
    Assert.assertEquals(storedTodo.isCompleted(), changedTodoCompeteStatus);
    // should not have changed
    Assert.assertEquals(storedTodo.getMessage(), originalTodoMessage);
  }

  @Test
  public void alterTodo_whenParameterTodoOnlyHasPropertyMessage_shouldOnlyAlterMessage() {
    final int someTodoId = 10;
    TodoVm todoVm = TodoVm.builder().message("altered").completed(null).build();

    String originalTodoMessage = "original message";
    boolean originalTodoCompeteStatus = true;
    User storedUser = User.builder().id(1).build();
    Todo storedTodo = Todo.builder().message(originalTodoMessage)
            .completed(originalTodoCompeteStatus).user(storedUser).build();

    when(userServiceMock.getUserId()).thenReturn(storedUser.getId());
    when(todoRepositoryMock.findById(someTodoId)).thenReturn(Optional.of(storedTodo)) ;
    when(todoRepositoryMock.save(storedTodo)).thenReturn(null); // return is ignored

    // act
    todoService.alterTodo(todoVm, someTodoId);

    // message should have been copied from todoVm
    Assert.assertNotEquals(storedTodo.getMessage(), originalTodoMessage);
    Assert.assertEquals(storedTodo.getMessage(), todoVm.getMessage());
    // should not have changed
    Assert.assertEquals(storedTodo.isCompleted(), originalTodoCompeteStatus);
  }


  @Test
  public void alterTodo_whenParameterTodoIsTheSameAsStoredTodo_shouldThrowNoChangeException() {
    final int someTodoId = 10;
    final String message = "same message";
    final boolean completed = true;
    TodoVm todoVm = TodoVm.builder().message(message).completed(completed).build();

    User storedUser = User.builder().id(1).build();
    Todo storedTodo = Todo.builder().message(message)
            .completed(completed).user(storedUser).build();

    when(userServiceMock.getUserId()).thenReturn(storedUser.getId());
    when(todoRepositoryMock.findById(someTodoId)).thenReturn(Optional.of(storedTodo)) ;
    when(todoRepositoryMock.save(storedTodo)).thenReturn(null); // return is ignored

    // act
    Exception exception = assertThrows(NoChangeException.class, () -> {
      todoService.alterTodo(todoVm, someTodoId);
    });
    Assert.assertTrue(exception.getMessage().contains("nothing was changed"));
  }

  @Test
  public void alterTodo_whenParameterTodoContainsNothing_shouldValidationException() {
    final int someTodoId = 10;
    TodoVm todoVm = TodoVm.builder().message(null).completed(null).build();
    Exception exception = assertThrows(ValidationException.class, () -> {
      todoService.alterTodo(todoVm, someTodoId);
    });
    Assert.assertTrue(exception.getMessage().contains(TodoService.noInputMessage));
  }

  @Test
  public void alterTodo_whenParameterTodoHasMessageLength1_shouldValidationException() {
    final int someTodoId = 10;
    TodoVm todoVm = TodoVm.builder().message("a").completed(null).build();
    Exception exception = assertThrows(ValidationException.class, () -> {
      todoService.alterTodo(todoVm, someTodoId);
    });
    Assert.assertTrue(exception.getMessage().contains(TodoService.inputTooSmallMessage));
  }

  @Test
  public void alterTodo_whenParameterTodoHasMessageLongerThen255_shouldValidationException() {
    final int someTodoId = 10;
    final String bigString =  IntStream.range(0, 300)
            .mapToObj(i -> Character.toString((char)'a'))
            .collect(Collectors.joining());
    TodoVm todoVm = TodoVm.builder().message(bigString).completed(null).build();
    Exception exception = assertThrows(ValidationException.class, () -> {
      todoService.alterTodo(todoVm, someTodoId);
    });
    Assert.assertTrue(exception.getMessage().contains(TodoService.inputTooBigMessage));
  }
}
