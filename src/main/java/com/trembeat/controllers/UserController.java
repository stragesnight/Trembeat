package com.trembeat.controllers;

import com.trembeat.domain.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @GetMapping("/login")
    public String getLogin() {
        return "user/login";
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return new ModelAndView("user/register", "user", new User());
    }

    @PostMapping("/register")
    public ModelAndView postRegister() {
        // TODO
        return null;
    }
}
