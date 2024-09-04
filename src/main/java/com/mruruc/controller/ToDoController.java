package com.mruruc.controller;

import com.mruruc.dto.TodoDto;
import com.mruruc.exceptions.AuthenticationNotContainsUsernameExceptions;
import com.mruruc.security.user_adapter.UserAdapter;
import com.mruruc.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/to-do")
public class ToDoController {
    private final TodoService todoService;

    @Autowired
    public ToDoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoDto> getTodos(Authentication authentication) {
        String userName = authentication.getName();
        if (!userName.isEmpty()) return todoService.findTodos(userName);
        throw new AuthenticationNotContainsUsernameExceptions("Current authentication instance does not contain username!");
    }

    @GetMapping("/{todoId}")
    public TodoDto getTodo(@PathVariable(name = "todoId") Long todoId) {
        return todoService.findTodoById(todoId);
    }

    @PostMapping
    public ResponseEntity<Void> createToDo(@RequestBody TodoDto todo, Authentication authentication) {
        UserAdapter userAdapter =
                (UserAdapter) authentication.getPrincipal();
        if (userAdapter.getUserId() == null) {
            throw new AuthenticationNotContainsUsernameExceptions("Current authentication instance does not contain username!");
        }

        todo.setUserId(userAdapter.getUserId());
        Long savedTodoId = todoService.saveTodo(todo);
        return ResponseEntity
                .created(URI.create(String.format("/api/v1/%s", savedTodoId)))
                .build();
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<Void> updateToDo(@PathVariable(name = "todoId") Long todoId,
                                           @RequestBody TodoDto todo) {
        todoService.updateTodo(todoId, todo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteToDo(@PathVariable(name = "todoId") Long todoId) {
        todoService.deleteToDo(todoId);
        return ResponseEntity.noContent().build();
    }

}
