package com.mruruc.validation.custome_validations;

import com.mruruc.validation.custome_validations.Impl.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@NotBlank(message = "Email can not be empty!")
public @interface ValidEmail {
    String message() default "Email is not valid. ex format mr.uruc@uruc.com";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
