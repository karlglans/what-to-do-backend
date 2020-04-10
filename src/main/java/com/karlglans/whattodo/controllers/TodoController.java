package com.karlglans.whattodo.controllers;

import com.karlglans.whattodo.entities.Todo;
import com.karlglans.whattodo.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/todos")
public class TodoController {

  @Autowired
  TodoService todoService;

  @GetMapping()
  ResponseEntity<List<Todo>> getTodos() {
    return new ResponseEntity<>(todoService.getTodos(), HttpStatus.OK);
  }

  @PostMapping(value = "", produces = "application/json")
  ResponseEntity<Todo> addTodos(@RequestBody Todo todo) {
    return new ResponseEntity<>(todoService.addTodo(todo), HttpStatus.CREATED);
  }
}