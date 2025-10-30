package com.mruruc.dto;

import com.mruruc.validation.custome_validations.Password;
import com.mruruc.validation.custome_validations.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegistrationRequest(
        @NotBlank(message = "first name can not be empty!")
        String firstName,
        @NotBlank(message = "last name can not be empty!")
        String lastName,
        @ValidEmail
        String email,
        @Password
        String password)
{}
