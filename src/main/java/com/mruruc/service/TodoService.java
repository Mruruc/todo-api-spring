package com.mruruc.service;

import com.mruruc.dto.TodoDto;
import com.mruruc.exceptions.EntityNotFoundException;
import com.mruruc.mapper.TodoMapper;
import com.mruruc.model.Todo;
import com.mruruc.repository.TodoRepository;
import com.mruruc.validation.dtoValidator.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository repository;
    private final DtoValidator<TodoDto> validator;
    private final TodoMapper mapper;


    public List<TodoDto> findTodos(String username) {
        return repository.findAllByUser_Email(username)
                .stream()
                .map(mapper::toDto)
                .toList();

    }

    public TodoDto findTodoById(Long todoId) {
        return repository
                .findById(todoId)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("ToDo Not Found"));
    }

    public Long saveTodo(TodoDto dto, Long userId) {
        validator.validate(dto);
        Todo todo = mapper.toEntity(dto, userId);
        return repository.save(todo).getId();
    }


    public void updateTodo(Long todoId, TodoDto dto) {
        validator.validate(dto);
        Todo todo = this.repository.findById(todoId)
                .orElseThrow(() -> new EntityNotFoundException("ToDo Not Found"));

        todo.setTitle(todo.getTitle());
        todo.setDescription(dto.description());
        todo.setEndDate(dto.endDate());

        repository.save(todo);
    }

    public void deleteToDo(Long todoId) {
        Todo todo = repository.getReferenceById(todoId);
        repository.delete(todo);
    }
}
