package com.trembeat.domain.viewmodels;

import com.trembeat.domain.models.*;
import lombok.Data;

import java.util.Date;

/**
 * User view model during profile viewing
 */
@Data
public class UserViewModel {
    private Long id;

    private String username;

    private String email;

    private String bio;

    private Date registrationDate;

    private FileEntityViewModel profilePicture;


    public UserViewModel(User user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        bio = user.getBio();
        registrationDate = user.getRegistrationDate();
        profilePicture = new FileEntityViewModel(user.getProfilePicture());
    }
}
