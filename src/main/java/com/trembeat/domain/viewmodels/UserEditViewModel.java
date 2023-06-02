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
@ValidPassword
@NoArgsConstructor
public class UserEditViewModel {

    private MultipartFile profilePicture;

    @NotNull
    @NotEmpty
    private String username;

    private String bio;

    private String password;

    private String passwordConfirmation;

    @NotNull
    @ValidEmail
    private String email;


    public UserEditViewModel(User user) {
        username = user.getUsername();
        bio = user.getBio();
        password = user.getPassword();
        email = user.getEmail();
    }
}
