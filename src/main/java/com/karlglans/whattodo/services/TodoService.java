package com.karlglans.whattodo.services;

import com.karlglans.whattodo.entities.Todo;
import com.karlglans.whattodo.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
  @Autowired
  TodoRepository todoRepo;

  @Autowired
  UserService userService;

  public List<Todo> getTodos() {
    int userId = userService.getUserId();

    List<Todo> ans = todoRepo.findAll();
    return ans;
  }

  public Todo addTodo(Todo todo) {
    return todoRepo.save(todo);
  }
}
