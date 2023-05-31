package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.SoundViewModel;
import com.trembeat.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class SoundRestController extends GenericContentController {
    @Autowired
    private SoundRepository _soundRepo;
    @Autowired
    private GenreRepository _genreRepo;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private SoundContentStore _soundContentStore;


    @PostMapping("/api/get-sound/{id}")
    public ResponseEntity<?> getSound(@PathVariable Long id) {
        Optional<Sound> optionalSound = _soundRepo.findById(id);

        if (optionalSound.isEmpty())
            return getResponse(HttpStatus.NO_CONTENT);

        Sound sound = optionalSound.get();
        InputStreamResource isr = new InputStreamResource(_soundContentStore.getContent(sound));

        return new ResponseEntity<>(isr, getHeaders(sound), HttpStatus.OK);
    }

    @PostMapping("/api/get-sound")
    public ResponseEntity<?> getAllSounds() {
        return new ResponseEntity<>(_soundRepo.findAll(), null, HttpStatus.OK);
    }

    @PostMapping("/api/put-sound")
    public ResponseEntity<?> putSound(
            Authentication auth,
            @ModelAttribute("sound") @Valid SoundViewModel soundViewModel) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return getResponse(HttpStatus.BAD_REQUEST);

        Sound sound = new Sound(
                soundViewModel.getTitle(),
                soundViewModel.getDescription(),
                // TODO: genre validation
                _genreRepo.findById(soundViewModel.getGenreId()).get(),
                user);
        // TODO: move this into constructor?
        sound.setMimeType(soundViewModel.getFile().getContentType());
        sound.setContentLength(soundViewModel.getFile().getSize());

        try {
            sound = _soundRepo.save(sound);
            _soundContentStore.setContent(sound, soundViewModel.getFile().getInputStream());
        } catch (Exception ex) {
            return getResponse(HttpStatus.BAD_REQUEST);
        }

        return getResponse(HttpStatus.OK);
    }

    @PostMapping("/api/patch-sound/{id}")
    public ResponseEntity<?> patchSound(
            Authentication auth,
            @PathVariable Long id,
            @ModelAttribute("sound") @Valid SoundViewModel soundViewModel) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return getResponse(HttpStatus.BAD_REQUEST);

        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return getResponse(HttpStatus.BAD_REQUEST);

        Sound sound = optionalSound.get();
        if (sound.getAuthor().getId() != user.getId())
            return getResponse(HttpStatus.FORBIDDEN);

        try {
            sound = _soundRepo.save(sound);
            // TODO: proper sound file editing
            _soundContentStore.setContent(sound, soundViewModel.getFile().getInputStream());
        } catch (Exception ex) {
            return getResponse(HttpStatus.BAD_REQUEST);
        }

        return getResponse(HttpStatus.OK);
    }

    @PostMapping("/api/delete-sound/{id}")
    public ResponseEntity<?> deleteSound(Authentication auth, @PathVariable Long id) {
        User user = (User)auth.getPrincipal();
        if (user == null)
            return getResponse(HttpStatus.BAD_REQUEST);

        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return getResponse(HttpStatus.BAD_REQUEST);

        Sound sound = optionalSound.get();
        if (sound.getAuthor().getId() != user.getId())
            return getResponse(HttpStatus.FORBIDDEN);

        _soundRepo.deleteById(id);
        _soundContentStore.unsetContent(sound);

        return getResponse(HttpStatus.OK);
    }
}
