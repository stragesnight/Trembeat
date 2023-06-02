package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;

@Controller
public class SoundController {
    @Autowired
    private SoundRepository _soundRepo;
    @Autowired
    private GenreRepository _genreRepo;


    @GetMapping("/sound")
    public String getIndex() {
        return "sound/index";
    }

    @GetMapping("/sound/upload")
    public ModelAndView getUpload(Authentication auth) {
        User user = (User)auth.getPrincipal();
        return new ModelAndView("sound/upload", Map.of(
                "sound", new SoundUploadViewModel(user.getId()),
                "genres", _genreRepo.findAll()));
    }

    @GetMapping("/sound/{id}")
    public ModelAndView getView(@PathVariable Long id) {
        Optional<Sound> optionalSound = _soundRepo.findById(id);
        // TODO: error page
        if (optionalSound.isEmpty())
            return new ModelAndView("home/index");

        return new ModelAndView("sound/view", "sound", new SoundViewModel(optionalSound.get()));
    }
}
