package com.trembeat.domain.viewmodels;

import com.trembeat.annotations.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * User view model
 */
@Data
@ValidPassword
@NoArgsConstructor
public class UserViewModel {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    private String bio;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String passwordConfirmation;

    @NotNull
    @ValidEmail
    private String email;
}
