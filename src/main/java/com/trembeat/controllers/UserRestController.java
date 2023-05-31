package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.ProfilePictureRepository;
import com.trembeat.domain.viewmodels.*;
import com.trembeat.services.ProfilePictureStorageService;
import com.trembeat.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UserRestController extends GenericContentController {
    @Autowired
    private ProfilePictureRepository _profilePictureRepo;
    @Autowired
    private UserService _userService;
    @Autowired
    private ProfilePictureStorageService _profilePictureStorageService;


    @PostMapping("/api/get-profile-picture/{id}")
    public ResponseEntity<?> getProfilePicture(@PathVariable Long id) {
        Optional<ProfilePicture> optionalProfilePicture = _profilePictureRepo.findById(id);

        if (optionalProfilePicture.isEmpty())
            return getResponse(HttpStatus.NO_CONTENT);

        ProfilePicture profilePicture = optionalProfilePicture.get();
        InputStreamResource isr = new InputStreamResource(_profilePictureStorageService.load(profilePicture.getId()));

        return new ResponseEntity<>(isr, getHeaders(profilePicture), HttpStatus.OK);
    }

    @PostMapping("/api/get-user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(_userService.findById(id), null, HttpStatus.OK);
    }

    @PostMapping("/api/put-user")
    public ResponseEntity<?> putUser(@ModelAttribute("user") @Valid UserRegisterViewModel viewModel) {
        if (!_userService.registerUser(viewModel))
            return getResponse(HttpStatus.BAD_REQUEST);

        return getResponse(HttpStatus.OK);
    }

    @PostMapping("/api/patch-user")
    public ResponseEntity<?> patchUser(
            Authentication auth,
            @PathVariable Long id,
            @ModelAttribute("user") @Valid UserEditViewModel viewModel) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return getResponse(HttpStatus.BAD_REQUEST);

        if (!_userService.updateUser(user.getId(), viewModel))
            return getResponse(HttpStatus.BAD_REQUEST);

        return getResponse(HttpStatus.OK);
    }

    @PostMapping("/api/delete-user")
    public ResponseEntity<?> deleteUser(Authentication auth) {
        User user = (User)auth.getPrincipal();
        if (user == null)
            return getResponse(HttpStatus.BAD_REQUEST);

        if (!_userService.deleteUser(user.getId()))
            return getResponse(HttpStatus.BAD_REQUEST);

        return getResponse(HttpStatus.OK);
    }
}
