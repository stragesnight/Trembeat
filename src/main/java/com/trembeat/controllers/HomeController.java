package com.trembeat.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/error")
    public ModelAndView getError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ModelAndView("error", "status", status);
    }
}
