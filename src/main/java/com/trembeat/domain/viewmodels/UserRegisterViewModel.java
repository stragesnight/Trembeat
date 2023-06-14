package com.trembeat.domain.viewmodels;

import com.trembeat.annotations.*;
import com.trembeat.domain.models.User;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * User view model during registration
 */
@Data
@ValidPassword(message = "error.unmatched_password")
@NoArgsConstructor
public class UserRegisterViewModel {
    @NotNull(message = "error.invalid_username")
    @NotEmpty(message = "error.invalid_username")
    private String username;

    @NotNull(message = "error.invalid_password")
    @NotEmpty(message = "error.invalid_password")
    private String password;

    @NotNull(message = "error.invalid_password")
    @NotEmpty(message = "error.invalid_password")
    private String passwordConfirmation;

    @NotNull(message = "error.invalid_email")
    @ValidEmail(message = "error.invalid_email")
    private String email;


    public UserRegisterViewModel(User user) {
        username = user.getUsername();
        password = user.getPassword();
        email = user.getEmail();
    }
}
