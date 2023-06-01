package com.trembeat.domain.viewmodels;

import com.trembeat.domain.models.*;
import lombok.Data;

import java.util.Date;

/**
 * User view model during profile viewing
 */
@Data
public class UserProfileViewModel {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private Date registrationDate;
    private ProfilePicture profilePicture;
    private Iterable<Sound> uploads;


    public UserProfileViewModel(User user, Iterable<Sound> uploads) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        bio = user.getBio();
        registrationDate = user.getRegistrationDate();
        profilePicture = user.getProfilePicture();
        this.uploads = uploads;
    }
}
