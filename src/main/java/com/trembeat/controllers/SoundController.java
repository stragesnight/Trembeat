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
    public ModelAndView getView(HttpServletRequest request, @PathVariable Long id) {
        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty()) {
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND);
            return new ModelAndView("redirect:/error");
        }

        Sound sound = optionalSound.get();

        return new ModelAndView("sound/view", Map.of(
                "sound", new SoundViewModel(sound),
                "comment", new CommentCreateViewModel(sound.getId())));
    }

    @Secured("ROLE_USER")
    @GetMapping("/sound/edit/{id}")
    public ModelAndView getEdit(Authentication auth, HttpServletRequest request, @PathVariable Long id) {
        User user = (User)auth.getPrincipal();
        Optional<Sound> optionalSound = _soundRepo.findById(id);

        if (optionalSound.isEmpty() || optionalSound.get().getAuthor().getId() != user.getId()) {
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.FORBIDDEN);
            return new ModelAndView("redirect:/error");
        }

        return new ModelAndView("sound/edit", Map.of(
                "sound", new SoundEditViewModel(optionalSound.get()),
                "genres", _genreRepo.findAll()
        ));
    }
}
