package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.*;
import com.trembeat.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
public class UserRestController extends GenericContentController {
    @Autowired
    private UserRepository _userRepo;
    @Autowired
    private ImageRepository _imageRepo;
    @Autowired
    private ImageStorageService _storageService;
    @Autowired
    private RoleRepository _roleRepo;
    private BCryptPasswordEncoder _passwordEncoder;

    private Role _roleUser = null;
    private Image _defaultPicture = null;



    public UserRestController() {
        _passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/api/get-profile-picture")
    public ResponseEntity<?> getProfilePicture(@RequestParam("id") Long id) {
        return getImageData(id);
    }

    @GetMapping("/api/get-user")
    public ResponseEntity<?> getUser(@RequestParam("id") Long id) {
        Optional<User> optionalUser = _userRepo.findById(id);
        if (optionalUser.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new UserViewModel(optionalUser.get()), null, HttpStatus.OK);
    }

    @PostMapping("/api/put-user")
    public ResponseEntity<?> putUser(@ModelAttribute("user") UserRegisterViewModel viewModel) {
        Map<String, String> errors = new HashMap<>();
        for (var violation : _validator.validate(viewModel))
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());

        User user = new User(
                viewModel.getUsername(),
                "",
                _passwordEncoder.encode(viewModel.getPassword()),
                viewModel.getEmail());

        if (_roleUser == null)
            _roleUser = _roleRepo.findByName("ROLE_USER").get();
        if (_defaultPicture == null)
            _defaultPicture = _imageRepo.findById(1L).get();

        user.addAuthority(_roleUser);
        user.setProfilePicture(_defaultPicture);

        try {
            _userRepo.save(user);
        } catch (Exception ex) {
            return null;
        }

        return getProfileRedirect(user);
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/patch-user")
    public ResponseEntity<?> patchUser(
            Authentication auth,
            @ModelAttribute("user") UserEditViewModel viewModel) {

        Map<String, String> errors = new HashMap<>();
        for (var violation : _validator.validate(viewModel))
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());

        User user = (User)auth.getPrincipal();
        if (user == null)
            errors.put("user", "error.invalid_user");

        if (!errors.isEmpty())
            return getErrorResponse(errors);

        user.setUsername(viewModel.getUsername());
        user.setBio(viewModel.getBio());

        if (viewModel.getPassword() != null && !viewModel.getPassword().isEmpty()) {
            user.setPassword(_passwordEncoder.encode(viewModel.getPassword()));
            user.setEmail(viewModel.getEmail());
        }

        if (viewModel.getProfilePicture() != null) {
            Image picture = new Image();
            picture.setMimeType(viewModel.getProfilePicture().getContentType());
            if (!_storageService.isAcceptedContentType(picture.getMimeType()))
                errors.put("profilePicture", "error.unaccepted_content_type");

            try {
                picture = _imageRepo.save(picture);
                _storageService.save(picture, viewModel.getProfilePicture().getInputStream());
                picture = _imageRepo.save(picture);
                user.setProfilePicture(picture);
            } catch (IOException e) {
                errors.put("cover", "error.invalid_cover");
            }
        }

        if (!errors.isEmpty())
            return getErrorResponse(errors);

        try {
            user = _userRepo.save(user);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

        return getProfileRedirect(user);
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/delete-user")
    public ResponseEntity<?> deleteUser(Authentication auth) {
        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            _storageService.delete(user.getProfilePicture());
            _userRepo.delete(user);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                new Response(null, "/"),
                null,
                HttpStatus.OK);
    }
}
