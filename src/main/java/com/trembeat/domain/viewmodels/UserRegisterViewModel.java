package com.trembeat.domain.viewmodels;

import com.trembeat.annotations.*;
import com.trembeat.domain.models.User;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * User view model during registration
 */
@Data
@ValidPassword
@NoArgsConstructor
public class UserRegisterViewModel {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String passwordConfirmation;

    @NotNull
    @ValidEmail
    private String email;


    public UserRegisterViewModel(User user) {
        username = user.getUsername();
        password = user.getPassword();
        email = user.getEmail();
    }
}
