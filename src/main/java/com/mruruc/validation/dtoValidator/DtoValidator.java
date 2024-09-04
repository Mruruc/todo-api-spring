package com.mruruc.validation.dtoValidator;

import com.mruruc.exceptions.DtoNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DtoValidator<T> {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    public void validate(T dto){
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(dto);
        if (!constraintViolations.isEmpty()) throw new DtoNotValidException(constraintViolations);
    }

}
