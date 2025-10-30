package com.mruruc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TodoDto(
        Long toDoId,
        String title,
        @NotBlank(message = "Description can not be empty.")
        String description,
        @Future(message = "Todo end date can not be in past.")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime endDate,
        boolean isCompleted,
        Long userId
) {
}
