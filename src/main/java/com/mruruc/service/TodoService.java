package com.mruruc.service;

import com.mruruc.dto.TodoDto;
import com.mruruc.exceptions.EntityNotFoundException;
import com.mruruc.model.Todo;
import com.mruruc.model.User;
import com.mruruc.repository.TodoRepository;
import com.mruruc.validation.dtoValidator.DtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository repository;
    private final DtoValidator<TodoDto> validator;

    @Autowired
    public TodoService(TodoRepository repository, DtoValidator<TodoDto> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<TodoDto> findTodos(String username) {
        return repository
                .findAllByUser_Email(username)
                .stream()
                .map(todo ->
                        TodoDto.builder()
                                .toDoId(todo.getToDoId())
                                .description(todo.getDescription())
                                .startDate(todo.getStartDate())
                                .endDate(todo.getEndDate())
                                .build()
                ).toList();

    }

    public TodoDto findTodoById(Long todoId) {
        return repository
                .findById(todoId)
                .map(todo ->
                        TodoDto.builder()
                                .toDoId(todo.getToDoId())
                                .description(todo.getDescription())
                                .startDate(todo.getStartDate())
                                .endDate(todo.getEndDate())
                                // .userId(todo.getUser().getUserId())
                                .build())
                .orElseThrow(() -> new EntityNotFoundException("ToDo Not Found"));
    }

    public Long saveTodo(TodoDto dto) {
        // validate Todo dto
        validator.validate(dto);
        // build Todo entity
        Todo todo = todoBuilder(dto);
        // save todo to database
        return repository.save(todo).getToDoId();
    }

    private Todo todoBuilder(TodoDto dto) {
        return Todo.builder()
                .description(dto.getDescription())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .user(
                        User.builder()
                                .userId(dto.getUserId())
                                .build()
                )
                .build();
    }

    public void deleteToDo(Long todoId) {
        repository.delete(
                Todo.builder()
                        .toDoId(findTodoById(todoId).getToDoId())
                        .build()
        );
    }

    public void updateTodo(Long todoId, TodoDto dto) {
        // validate the Todo dto update
        validator.validate(dto);
        // find by id the actual todo entity
        Todo todoById =
                this.repository.findById(todoId)
                        .orElseThrow(() -> new EntityNotFoundException("ToDo Not Found"));
        // update the actual todo entity
        todoById.setDescription(dto.getDescription());
        todoById.setStartDate(dto.getStartDate());
        todoById.setEndDate(dto.getEndDate());
        // save to database
        repository.save(todoById);
    }
}
