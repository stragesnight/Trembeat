package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.SoundUploadViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView getUpload(Authentication auth) {
        User user = (User)auth.getPrincipal();
        return new ModelAndView("sound/upload", Map.of(
                "sound", new SoundUploadViewModel(user.getId()),
                "genres", _genreRepo.findAll()));
    }
}
