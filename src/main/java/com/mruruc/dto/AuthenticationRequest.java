package com.mruruc.dto;

import com.mruruc.validation.custome_validations.Password;
import com.mruruc.validation.custome_validations.ValidEmail;
import lombok.Builder;


@Builder
public record AuthenticationRequest(
        @ValidEmail
        String email,
        @Password
        String password)
{}
