package com.mruruc.handeler.exception_dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {
    private HttpStatus httpStatus;
    private String message;
    private String description;
    private String error;
    private Set<String> validationErrors;
    private Map<String,String> errors;
}
