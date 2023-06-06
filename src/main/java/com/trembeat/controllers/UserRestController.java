package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.viewmodels.*;
import com.trembeat.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserRestController extends GenericContentController {
    @Autowired
    private UserService _userService;


    @GetMapping("/api/get-profile-picture")
    public ResponseEntity<?> getProfilePicture(@RequestParam("id") Long id) {
        return getImageData(id);
    }

    @GetMapping("/api/get-user")
    public ResponseEntity<?> getUser(@RequestParam("id") Long id) {
        User user = _userService.findById(id);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(new UserViewModel(user), null, HttpStatus.OK);
    }

    @PostMapping("/api/put-user")
    public ResponseEntity<?> putUser(@ModelAttribute("user") @Valid UserRegisterViewModel viewModel) {
        if (!_userService.registerUser(viewModel))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = (User)_userService.loadUserByUsername(viewModel.getUsername());
        return getProfileRedirect(user);
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/patch-user")
    public ResponseEntity<?> patchUser(
            Authentication auth,
            @ModelAttribute("user") @Valid UserEditViewModel viewModel) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!_userService.updateUser(user.getId(), viewModel))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return getProfileRedirect(user);
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/delete-user")
    public ResponseEntity<?> deleteUser(Authentication auth) {
        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!_userService.deleteUser(user.getId()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(
                new Response(null, "/"),
                null,
                HttpStatus.OK);
    }
}
