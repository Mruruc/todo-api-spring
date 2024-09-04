package com.mruruc.dto;

import com.mruruc.validation.custome_validations.Password;
import com.mruruc.validation.custome_validations.ValidEmail;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
    @ValidEmail
    private String email;
    @Password
    private String password;
}
