package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.services.SoundService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
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
    public ModelAndView getUpload(Principal principal) {
        User user = (User)principal;

        // TODO: error page
        if (user == null)
            return new ModelAndView("home/index");

        Sound sound = new Sound();
        sound.setAuthor(user);
        // TODO: ViewModel genre handling
        sound.setGenre(new Genre());

        return new ModelAndView("sound/upload", Map.of(
                "sound", sound,
                "genres", _genreRepo.findAll()
        ));
    }

    @PostMapping("/sound/upload")
    public String postUpload(
            @ModelAttribute("sound") @Valid Sound sound,
            @RequestParam("file") MultipartFile file) {
        return _soundService.addSound(sound, file)
                ? "home/index"
                : "sound/upload";
    }
}
