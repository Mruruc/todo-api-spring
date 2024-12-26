package com.mruruc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {
    Long toDoId;
    @NotBlank(message = "Description can not be empty.")
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @FutureOrPresent(message = "Todo start date can not be in past!")
    LocalDateTime startDate;
    @Future(message = "Todo end date can not be in past.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime endDate;
    Long userId;
}
