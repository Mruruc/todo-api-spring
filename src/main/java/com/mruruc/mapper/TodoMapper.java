package com.mruruc.mapper;

import com.mruruc.dto.TodoDto;
import com.mruruc.model.Todo;
import com.mruruc.model.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TodoMapper {

    public Todo toEntity(TodoDto dto, Long userId) {
        Objects.requireNonNull(dto);
        var user = User.builder()
                .userId(userId)
                .build();
        return Todo.builder()
                .title(dto.title())
                .description(dto.description())
                .endDate(dto.endDate())
                .isCompleted(dto.isCompleted())
                .user(user)
                .build();
    }

    public TodoDto toDto(Todo todo) {
        return TodoDto.builder()
                .toDoId(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .endDate(todo.getEndDate())
                .isCompleted(todo.isCompleted())
                .userId(todo.getUser().getUserId())
                .build();
    }
}
