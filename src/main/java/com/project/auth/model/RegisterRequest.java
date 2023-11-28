package com.project.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Size(max = 45, message = USER_NAME_SHOULD_BE_LESS_THAN_45_CHARS)
    @NotBlank(message = USER_NAME_SHOULD_PRESENT_AND_NOT_BLANK)
    private String firstname;
    @Size(max = 45, message = USER_SURNAME_SHOULD_BE_LESS_THAN_45_CHARS)
    @NotBlank(message = USER_SURNAME_SHOULD_PRESENT_AND_NOT_BLANK)
    private String lastname;
    @Size(max = 45, message = USER_EMAIL_SHOULD_BE_LESS_THAN_45_CHARS)
    @NotBlank(message = USER_EMAIL_SHOULD_PRESENT_AND_NOT_BLANK)
    @Email(message = EMAIL_SHOULD_BE_VALID)
    private String email;
    @NotBlank(message = PASSWORD_SHOULD_PRESENT_AND_NOT_BE_BLANK)
    private String password;
}