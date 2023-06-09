package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;


@Controller
public class UserController {
    @Autowired
    private UserRepository _userRepo;
    @Autowired
    private GenreRepository _genreRepo;


    @GetMapping("/login")
    public String getLogin() {
        return "user/login";
    }

    @GetMapping("/logout")
    public String getLogout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        return new ModelAndView("user/register", "user", new UserRegisterViewModel());
    }

    @Secured("ROLE_USER")
    @GetMapping("/edit-profile")
    public ModelAndView getEditProfile(Authentication auth) {
        UserEditViewModel viewModel = new UserEditViewModel((User)auth.getPrincipal());
        return new ModelAndView("user/edit", "user", viewModel);
    }

    @GetMapping("/user/{userId}")
    public ModelAndView getView(HttpServletRequest request, @PathVariable Long userId) {
        Optional<User> optionalUser = _userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND);
            return new ModelAndView("redirect:/error");
        }

        UserViewModel viewModel = new UserViewModel(optionalUser.get());
        return new ModelAndView("user/view", Map.of(
                "viewedUser", viewModel,
                "genres", _genreRepo.findAll()));
    }
}
