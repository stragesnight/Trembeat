package com.trembeat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @GetMapping("/")
    public String getIndex() {
        return "home/index";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "home/about";
    }
}
