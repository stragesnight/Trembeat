package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.SoundViewModel;
import com.trembeat.services.SoundService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class SoundController {
    @Autowired
    private SoundService _soundService;
    @Autowired
    private GenreRepository _genreRepo;


    @GetMapping("/sound")
    public ModelAndView getIndex() {
        return new ModelAndView("sound/index", "sounds", _soundService.findAll());
    }

    @GetMapping("/sound/upload")
    public ModelAndView getUpload(Authentication auth) {
        User user = (User)auth.getPrincipal();
        return new ModelAndView("sound/upload", Map.of(
                "sound", new SoundViewModel(user.getId()),
                "genres", _genreRepo.findAll()));
    }

    @PostMapping("/sound/upload")
    public String postUpload(
            Authentication auth,
            @ModelAttribute("sound") @Valid SoundViewModel soundViewModel) {

        User user = (User)auth.getPrincipal();
        // TODO: error page
        if (user == null)
            return "sound/upload";

        return _soundService.addSound(soundViewModel, user)
                ? "home/index"
                : "sound/upload";
    }
}
