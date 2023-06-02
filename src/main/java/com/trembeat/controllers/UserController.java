package com.trembeat.controllers;

import com.trembeat.configuration.WebConfiguration;
import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.SoundRepository;
import com.trembeat.domain.viewmodels.*;
import com.trembeat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService _userService;
    @Autowired
    private SoundRepository _soundRepo;


    @GetMapping("/login")
    public String getLogin() {
        return "user/login";
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return new ModelAndView("user/register", "user", new UserRegisterViewModel());
    }

    @GetMapping("/edit-profile")
    public ModelAndView getEditProfile(Authentication auth) {
        UserEditViewModel viewModel = new UserEditViewModel((User)auth.getPrincipal());
        return new ModelAndView("user/edit", "user", viewModel);
    }

    @GetMapping("/user/{userId}")
    public ModelAndView getView(@PathVariable Long userId) {
        User user = _userService.findById(userId);
        // TODO: error page
        if (user == null)
            return new ModelAndView("home/index");

        Iterable<Sound> sounds = _soundRepo.findAllByAuthor(
                user, PageRequest.of(0, WebConfiguration.PAGE_LEN));
        UserViewModel viewModel = new UserViewModel(user);

        return new ModelAndView("user/view", Map.of(
                "viewedUser", viewModel,
                "uploads", sounds));
    }
}
