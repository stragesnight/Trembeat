package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.services.SoundService;
import com.trembeat.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public ModelAndView getUpload() {
        return new ModelAndView("sound/upload", Map.of(
                "sound", new Sound(),
                "genres", _genreRepo.findAll()
        ));
    }

    @PostMapping("/sound/upload")
    public String postUpload(
            Authentication auth,
            @ModelAttribute("sound") @Valid Sound sound,
            @RequestParam("file") MultipartFile file,
            @RequestParam("genreId") Long genreId) {

        User user = (User)auth.getPrincipal();
        // TODO: error page
        if (user == null)
            return "sound/upload";

        sound.setAuthor(user);
        // TODO: ViewModel genre handling
        sound.setGenre(_genreRepo.findById(genreId).get());

        return _soundService.addSound(sound, file)
                ? "home/index"
                : "sound/upload";
    }
}
