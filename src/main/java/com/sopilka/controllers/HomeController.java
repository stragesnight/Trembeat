package com.sopilka.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @GetMapping("/")
    public ModelAndView getIndex() {
        return new ModelAndView("layout", "content", "home/index");
    }

    @RequestMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("layout", "content", "home/about");
    }
}
