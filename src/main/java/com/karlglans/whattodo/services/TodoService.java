package com.karlglans.whattodo.services;

import com.karlglans.whattodo.entities.Todo;
import com.karlglans.whattodo.repositories.TodoRepository;

import com.karlglans.whattodo.services.exceptions.IlligalActionException;
import com.karlglans.whattodo.services.exceptions.MissingItemException;
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

  public int deleteTodo(int noteId) {
    int userId = userService.getUserId();
    Optional<Todo> todoOptional = todoRepo.findById(noteId);
    if (!todoOptional.isPresent()) {
      throw new MissingItemException(String.format("User %d is trying to delete missing note %d",
        userId, noteId));
    }
    if (todoOptional.get().getUser().getId() != userId) {
      throw new IlligalActionException(String.format("User %d is trying to delete someone else's note %d",
        userId, noteId));
    }
    todoRepo.deleteById(noteId);
    return noteId;
  }
}
