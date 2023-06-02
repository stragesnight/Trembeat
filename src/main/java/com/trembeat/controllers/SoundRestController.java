package com.trembeat.controllers;

import com.trembeat.configuration.WebConfiguration;
import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.*;
import com.trembeat.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class SoundRestController extends GenericContentController {
    @Autowired
    private SoundRepository _soundRepo;
    @Autowired
    private GenreRepository _genreRepo;
    @Autowired
    private SoundStorageService _soundStorageService;


    @GetMapping("/api/get-sound-data")
    public ResponseEntity<?> getSoundData(@RequestParam("id") Long id) {
        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        Sound sound = optionalSound.get();
        InputStreamResource isr = new InputStreamResource(_soundStorageService.load(sound));

        return new ResponseEntity<>(isr, getHeaders(sound), HttpStatus.OK);
    }

    @GetMapping("/api/get-cover")
    public ResponseEntity<?> getCover(@RequestParam("id") Long id) {
        return getImageData(id);
    }

    @GetMapping("/api/get-sound")
    public ResponseEntity<?> getSound(@RequestParam("id") Long id) {
        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        Sound sound = optionalSound.get();
        SoundViewModel viewModel = new SoundViewModel(sound);

        return new ResponseEntity<>(new Response(viewModel), getHeaders(sound), HttpStatus.OK);
    }

    @GetMapping("/api/get-sounds")
    public ResponseEntity<?> getSounds(
            @RequestParam("title") Optional<String> title,
            @RequestParam("page") Optional<Integer> page) {

        Stream<SoundViewModel> sounds = _soundRepo.findAllByTitleLikeIgnoreCase(
                String.format("%%%s%%", title.orElse("")),
                PageRequest.of(page.orElse(0), WebConfiguration.PAGE_LEN))
                .stream()
                .map(SoundViewModel::new);

        return new ResponseEntity<>(new Response(sounds), null, HttpStatus.OK);
    }

    @PostMapping("/api/put-sound")
    public ResponseEntity<?> putSound(
            Authentication auth,
            @ModelAttribute("sound") @Valid SoundUploadViewModel viewModel) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!_soundStorageService.isAcceptedContentType(viewModel.getFile().getContentType()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Sound sound = new Sound(
                viewModel.getTitle(),
                viewModel.getDescription(),
                // TODO: genre validation
                _genreRepo.findById(viewModel.getGenreId()).get(),
                user);

        // TODO: move this into constructor?
        sound.setMimeType(viewModel.getFile().getContentType());
        sound.setContentLength(viewModel.getFile().getSize());

        if (viewModel.getCover() != null && !updateCover(new Image(), viewModel.getCover(), sound))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            sound = _soundRepo.save(sound);
            _soundStorageService.save(sound, viewModel.getFile().getInputStream());
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return getProfileRedirect(user);
    }

    @PostMapping("/api/patch-sound")
    public ResponseEntity<?> patchSound(
            Authentication auth,
            @RequestParam("id") Long id,
            @ModelAttribute("sound") @Valid SoundUploadViewModel viewModel) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (!_soundStorageService.isAcceptedContentType(viewModel.getFile().getContentType()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Sound sound = optionalSound.get();
        if (sound.getAuthor().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        if (viewModel.getCover() != null && !updateCover(new Image(), viewModel.getCover(), sound))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        try {
            // TODO: proper sound file editing
            sound = _soundRepo.save(sound);
            _soundStorageService.save(sound, viewModel.getFile().getInputStream());
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return getProfileRedirect(user);
    }

    @PostMapping("/api/delete-sound")
    public ResponseEntity<?> deleteSound(Authentication auth, @RequestParam("id") Long id) {
        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Sound sound = optionalSound.get();
        if (sound.getAuthor().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        _soundStorageService.delete(sound);
        _soundRepo.deleteById(id);

        return getProfileRedirect(user);
    }

    private boolean updateCover(Image cover, MultipartFile file, Sound sound) {
        cover.setMimeType(file.getContentType());
        if (!_soundStorageService.isAcceptedContentType(cover.getMimeType()))
            return false;

        cover.setContentLength(file.getSize());
        cover = _imageRepo.save(cover);

        sound.setCover(cover);

        try {
            _imageStorageService.save(cover, file.getInputStream());
        } catch (IOException ex) {
            return false;
        }

        return true;
    }
}
