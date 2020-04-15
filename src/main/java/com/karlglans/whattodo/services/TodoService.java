package com.karlglans.whattodo.services;

import com.karlglans.whattodo.controllers.vm.TodoVm;
import com.karlglans.whattodo.entities.Todo;
import com.karlglans.whattodo.repositories.TodoRepository;

import com.karlglans.whattodo.services.exceptions.IlligalActionException;
import com.karlglans.whattodo.services.exceptions.MissingItemException;
import com.karlglans.whattodo.services.exceptions.NoChangeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
  private final TodoRepository todoRepo;
  private final UserService userService;

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
    if (todoVm.getCompleted() != null) {
      todo.setCompleted(todoVm.getCompleted());
      isChanged = true;
    }
    return isChanged;
  }

  public Todo alterTodo(TodoVm todoVm, int todoId) {
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
}
