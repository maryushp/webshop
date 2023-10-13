package com.project.user.model;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    @Size(max = 45, message = USER_EMAIL_SHOULD_BE_LESS_THAN_45_CHARS)
    @NotBlank(message = USER_EMAIL_SHOULD_PRESENT_AND_NOT_BLANK)
    private String email;
    @Size(max = 45, message = USER_PASSWORD_SHOULD_BE_LESS_THAN_45_CHARS)
    @NotBlank(message = USER_PASSWORD_SHOULD_PRESENT_AND_NOT_BLANK)
    private String password;
    @Size(max = 45, message = USER_NAME_SHOULD_BE_LESS_THAN_45_CHARS)
    @NotBlank(message = USER_NAME_SHOULD_PRESENT_AND_NOT_BLANK)
    private String name;
    @Size(max = 45, message = USER_SURNAME_SHOULD_BE_LESS_THAN_45_CHARS)
    @NotBlank(message = USER_SURNAME_SHOULD_PRESENT_AND_NOT_BLANK)
    private String surname;
    @NotNull(message = USER_ROLE_SHOULD_PRESENT)
    private Role role;
}