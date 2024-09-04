package com.mruruc.exceptions;

import jakarta.validation.ConstraintViolation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DtoNotValidException extends RuntimeException {
    private final Set<? extends ConstraintViolation<?>> violations;

    public <T> DtoNotValidException(Set<? extends ConstraintViolation<?>> violations) {
        this.violations = violations;
    }

    public Map<String, String> getViolationsMap() {
        HashMap<String, String> violationsMap = new HashMap<>();

        for (ConstraintViolation<?> constraintViolation : violations) {
            violationsMap.put(
                    constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getMessage());
        }

        return violationsMap;
    }
}
