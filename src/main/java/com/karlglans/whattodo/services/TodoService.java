package com.karlglans.whattodo.services;

import com.karlglans.whattodo.entities.Todo;
import com.karlglans.whattodo.repositories.TodoRepository;

import com.karlglans.whattodo.services.exceptions.MissingItemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

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

  public int deleteTodo(int id) {
    try {
      todoRepo.deleteById(id);
    } catch (EmptyResultDataAccessException empty) {
      throw new MissingItemException("Trying to delete note" + id);
    }
    return id;
  }
}
