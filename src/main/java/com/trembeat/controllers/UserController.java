package com.trembeat.controllers;

import com.trembeat.domain.models.User;
import com.trembeat.domain.viewmodels.UserEditViewModel;
import com.trembeat.domain.viewmodels.UserRegisterViewModel;
import com.trembeat.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    private UserService _userService;


    @GetMapping("/login")
    public String getLogin() {
        return "user/login";
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return new ModelAndView("user/register", "user", new UserRegisterViewModel());
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("user") @Valid UserRegisterViewModel viewModel) {
        return _userService.registerUser(viewModel)
                ? "home/index"
                : "user/register";
    }

    @GetMapping("/edit-profile")
    public ModelAndView getEditProfile(Authentication auth) {
        UserEditViewModel viewModel = new UserEditViewModel((User)auth.getPrincipal());
        return new ModelAndView("user/edit", "user", viewModel);
    }

    @PostMapping("/edit-profile")
    public String postEditProfile(
            Authentication auth,
            @ModelAttribute("user") @Valid UserEditViewModel viewModel) {

        User user = (User)auth.getPrincipal();
        return _userService.updateUser(user.getId(), viewModel)
                ? "home/index"
                : "user/edit";
    }
}
