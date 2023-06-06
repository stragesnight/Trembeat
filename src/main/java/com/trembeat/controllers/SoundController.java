package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.*;
import jakarta.annotation.security.RolesAllowed;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ModelAndView getIndex() {
        return new ModelAndView("sound/index", "orderFields", Sound.orderableFields);
    }

    @Secured("ROLE_USER")
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

        Sound sound = optionalSound.get();

        return new ModelAndView("sound/view", Map.of(
                "sound", new SoundViewModel(sound),
                "comment", new CommentCreateViewModel(sound.getId())));
    }

    @Secured("ROLE_USER")
    @GetMapping("/sound/edit/{id}")
    public ModelAndView getEdit(Authentication auth, @PathVariable Long id) {
        User user = (User)auth.getPrincipal();
        Optional<Sound> optionalSound = _soundRepo.findById(id);
        // TODO: error page
        if (optionalSound.isEmpty() || optionalSound.get().getAuthor().getId() != user.getId())
            return new ModelAndView("home/index");

        return new ModelAndView("sound/edit", Map.of(
                "sound", new SoundEditViewModel(optionalSound.get()),
                "genres", _genreRepo.findAll()
        ));
    }
}
