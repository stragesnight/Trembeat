package com.trembeat.controllers;

import com.trembeat.domain.models.User;
import com.trembeat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new ModelAndView("user/register", "user", new User());
    }

    @PostMapping("/register")
    public String postRegister(User user) {
        return _userService.registerUser(user)
                ? "home/index"
                : "user/register";
    }
}
