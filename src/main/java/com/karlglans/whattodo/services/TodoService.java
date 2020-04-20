package com.karlglans.whattodo.services;

import com.karlglans.whattodo.controllers.vm.TodoVm;
import com.karlglans.whattodo.entities.Todo;
import com.karlglans.whattodo.repositories.TodoRepository;

import com.karlglans.whattodo.services.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {
  private final TodoRepository todoRepo;
  private final UserService userService;

  static final String noInputMessage = "no input";
  static final String inputTooSmallMessage = "no message is too small";
  static final String inputTooBigMessage = "no message is too big";

  @Autowired
  public TodoService(TodoRepository todoRepo, UserService userService) {
    this.todoRepo = todoRepo;
    this.userService = userService;
  }

  public List<Todo> getTodos() {
    int userId = userService.getUserId();
    return todoRepo.findAllByUserId(userId);
  }

  public Todo addTodo(Todo todo) {
    todo.setUser(userService.getUser());
    return todoRepo.save(todo);
  }

  public int deleteTodo(int todoId) {
    int userId = userService.getUserId();
    Optional<Todo> todoOptional = todoRepo.findById(todoId);
    if (!todoOptional.isPresent()) {
      throw new MissingItemException(String.format("User %d is trying to delete missing todo %d",
        userId, todoId));
    }
    if (todoOptional.get().getUser().getId() != userId) {
      throw new IlligalActionException(String.format("User %d is trying to delete someone else's todo %d",
        userId, todoId));
    }
    todoRepo.deleteById(todoId);
    return todoId;
  }

  /**
   * Will copy properties from view model to model. The view model might be empty.
   * @return true if anything is been copied
   */
  private boolean copyFromViewModel(Todo todo, TodoVm todoVm) {
    boolean isChanged = false;
    if (todoVm.getMessage() != null && todoVm.getMessage().compareTo(todo.getMessage()) != 0) {
      todo.setMessage(todoVm.getMessage());
      isChanged = true;
    }
    if (todoVm.getCompleted() != null && todoVm.getCompleted() != todo.isCompleted()) {
      todo.setCompleted(todoVm.getCompleted());
      isChanged = true;
    }
    return isChanged;
  }

  /**
   * TodoVm is validated in a special way since some parts of the object are allowed to be null.
   */
  private void validateTodoVM(TodoVm todoVm) {
    // a complex validation. Could have either a complete stare or a message. If both is missing then validation will fail
    if (todoVm.getCompleted() == null) {
      if (todoVm.getMessage() == null) {
        throw new ValidationException(noInputMessage);
      }
      if (todoVm.getMessage().length() == 1) {
        throw new ValidationException(inputTooSmallMessage);
      }
      if (todoVm.getMessage().length() > 256) {
        throw new ValidationException(inputTooBigMessage);
      }
    }
  }

  public Todo alterTodo(TodoVm todoVm, int todoId) {
    validateTodoVM(todoVm);
    int userId = userService.getUserId();
    Optional<Todo> todoOptional = todoRepo.findById(todoId);
    if (!todoOptional.isPresent()) {
      throw new MissingItemException(String.format("User %d is trying to alter missing todo %d",
              userId, todoId));
    }
    Todo todo = todoOptional.get();
    if (todo.getUser().getId() != userId) {
      throw new IlligalActionException(String.format("User %d is trying to alter someone else's todo %d",
              userId, todoId));
    }
    if (copyFromViewModel(todo, todoVm)) {
      todoRepo.save(todo);
    } else {
      throw new NoChangeException(String.format("nothing was changed when used %d asked updated todo %d",
              userId, todoId));
    }
    return todo;
  }

  private boolean todosIsAllCompleted(List<Todo> todos) {
    return todos.stream()
            .filter(todo -> !todo.isCompleted())
            .collect(Collectors.toList())
            .size() == 0;
  }

  private boolean setCompleted(List<Todo> todos, boolean completed) {
    todos.forEach(todo -> {
      todo.setCompleted(completed);
    });
    todoRepo.saveAll(todos);
    return completed;
  }

  /**
   * Will make all todos have the same complete status.
   * If one todo is incomplete then all will be put to compete.
   * If all todo is complete then all will be put to incomplete.
   * @return the complete status for all todos.
   */
  public boolean toggleAllTodosCompleteStatus() {
    int userId = userService.getUserId();
    List<Todo> todos = todoRepo.findAllByUserId(userId);
    if (todos.isEmpty()) throw new NoChangeException(String.format("no todos found for toggle by user %d", userId));
    boolean completed = todosIsAllCompleted(todos);
    return setCompleted(todos, !completed);
  }

  @Transactional
  public void deleteCompleted() {
    int userId = userService.getUserId();
    if (todoRepo.deleteTodoByUserIdAndCompletedIsTrue(userId) == 0)
      throw new NoChangeException(String.format("no todos found for delete-completed for user %d", userId));
  }
}
