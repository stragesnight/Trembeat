package com.trembeat.controllers;

import com.trembeat.domain.models.Sound;
import com.trembeat.domain.models.User;
import com.trembeat.domain.repository.GenreRepository;
import com.trembeat.domain.repository.SoundRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

@Controller
public class SoundController {
    @Autowired
    private SoundRepository _soundRepo;
    @Autowired
    private GenreRepository _genreRepo;


    @GetMapping("/sound")
    public ModelAndView getIndex() {
        return new ModelAndView("sound/index", "sounds", _soundRepo.findAll());
    }

    @GetMapping("/sound/upload")
    public ModelAndView getUpload(Principal principal) {
        User user = (User)principal;

        // TODO: error page
        if (user == null)
            return new ModelAndView("home/index");

        Sound sound = new Sound();
        sound.setAuthor(user);

        return new ModelAndView("sound/upload", Map.of(
                "sound", sound,
                "genres", _genreRepo.findAll()
        ));
    }

    @PostMapping("/sound/upload")
    public ModelAndView postUpload(@ModelAttribute("sound") @Valid Sound sound) {
        // TODO
        return null;
    }
}
