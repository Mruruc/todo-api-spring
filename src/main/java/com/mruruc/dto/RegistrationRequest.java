package com.mruruc.dto;

import com.mruruc.validation.custome_validations.Password;
import com.mruruc.validation.custome_validations.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
    @NotBlank(message = "first name can not be empty!")
    private String firstName;
    @NotBlank(message = "last name can not be empty!")
    private String lastName;
    @ValidEmail
    private String email;
    @Password
    private String password;
    @Password
    private String passwordRetype;
}
