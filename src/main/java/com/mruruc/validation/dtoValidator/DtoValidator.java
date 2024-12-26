package com.mruruc.validation.dtoValidator;

import com.mruruc.exceptions.DtoNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DtoValidator<T> {
    private final Validator validator;

    public void validate(T dto) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(dto);
        if (!constraintViolations.isEmpty()) throw new DtoNotValidException(constraintViolations);
    }

}
