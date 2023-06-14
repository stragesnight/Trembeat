package com.trembeat.domain.viewmodels;

import com.trembeat.annotations.*;
import com.trembeat.domain.models.User;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * User view model during profile editing
 */
@Data
@ValidPassword(message = "error.unmatched_password")
@NoArgsConstructor
public class UserEditViewModel {

    private MultipartFile profilePicture;

    @NotNull(message = "error.invalid_username")
    @NotEmpty(message = "error.invalid_username")
    private String username;

    private String bio;

    private String password;

    private String passwordConfirmation;

    @NotNull(message = "error.invalid_email")
    @ValidEmail(message = "error.invalid_email")
    private String email;


    public UserEditViewModel(User user) {
        username = user.getUsername();
        bio = user.getBio();
        password = user.getPassword();
        email = user.getEmail();
    }
}
