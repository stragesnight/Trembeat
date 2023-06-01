package com.trembeat.controllers;

import com.trembeat.configuration.WebConfiguration;
import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.SoundViewModel;
import com.trembeat.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
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
    private SoundStorageService _storageService;


    @GetMapping("/api/get-sound/{id}")
    public ResponseEntity<?> getSound(@PathVariable Long id) {
        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        Sound sound = optionalSound.get();
        InputStreamResource isr = new InputStreamResource(_storageService.load(sound));

        return new ResponseEntity<>(isr, getHeaders(sound), HttpStatus.OK);
    }

    @GetMapping("/api/get-sounds")
    public ResponseEntity<?> getSounds(
            @RequestParam("title") String title,
            @RequestParam("page") Optional<Integer> page) {

        Page<Sound> sounds = _soundRepo.findAllByTitleLikeIgnoreCase(
                title, PageRequest.of(page.orElse(0), WebConfiguration.PAGE_LEN));

        return new ResponseEntity<>(sounds, null, HttpStatus.OK);
    }

    @PostMapping("/api/put-sound")
    public ResponseEntity<?> putSound(
            Authentication auth,
            @ModelAttribute("sound") @Valid SoundViewModel soundViewModel) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!_storageService.isAcceptedContentType(soundViewModel.getFile().getContentType()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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
            _storageService.save(sound, soundViewModel.getFile().getInputStream());
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/patch-sound/{id}")
    public ResponseEntity<?> patchSound(
            Authentication auth,
            @PathVariable Long id,
            @ModelAttribute("sound") @Valid SoundViewModel soundViewModel) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!_storageService.isAcceptedContentType(soundViewModel.getFile().getContentType()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Sound sound = optionalSound.get();
        if (sound.getAuthor().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        try {
            // TODO: proper sound file editing
            sound = _soundRepo.save(sound);
            _storageService.save(sound, soundViewModel.getFile().getInputStream());
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/delete-sound/{id}")
    public ResponseEntity<?> deleteSound(Authentication auth, @PathVariable Long id) {
        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Sound sound = optionalSound.get();
        if (sound.getAuthor().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        _storageService.delete(sound);
        _soundRepo.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
