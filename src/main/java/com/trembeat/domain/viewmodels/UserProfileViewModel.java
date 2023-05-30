package com.trembeat.domain.viewmodels;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.SoundRepository;
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
    private Iterable<Sound> uploads;


    public UserProfileViewModel(User user, SoundRepository repository) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        bio = user.getBio();
        registrationDate = user.getRegistrationDate();
        uploads = repository.findAllByAuthorId(user.getId());
    }
}
