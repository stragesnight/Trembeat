package com.trembeat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("layout", "content", "user/login");
    }
}
