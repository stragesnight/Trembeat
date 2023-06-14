package com.trembeat.controllers;

import com.trembeat.configuration.WebConfiguration;
import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.*;
import com.trembeat.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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

        try {
            Sound sound = optionalSound.get();
            InputStreamResource isr = new InputStreamResource(_soundStorageService.load(sound));
            return new ResponseEntity<>(isr, getHeaders(sound), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("authorId") Optional<Long> authorId,
            @RequestParam("orderby") Optional<String> orderby,
            @RequestParam("asc") Optional<Boolean> orderAsc) {

        Sort sort = Sort.unsorted();
        if (orderby.isPresent() && Sound.orderableFields.contains(orderby.get())) {
            sort = Sort.by(
                    orderAsc.orElse(false) ? Sort.Direction.ASC : Sort.Direction.DESC,
                    orderby.get());
        }

        PageRequest pageRequest = PageRequest.of(
                page.orElse(0),
                WebConfiguration.PAGE_LEN,
                sort);

        Page<Sound> sounds = null;
        String titleString = String.format("%%%s%%", title.orElse(""));

        if (title.isPresent() && authorId.isPresent()) {
            sounds = _soundRepo.findAllByTitleLikeIgnoreCaseAndAuthor_Id(
                    titleString,
                    authorId.get(),
                    pageRequest);
        } else if (title.isPresent()) {
            sounds = _soundRepo.findAllByTitleLikeIgnoreCase(
                    titleString,
                    pageRequest);
        } else if (authorId.isPresent()) {
            sounds = _soundRepo.findAllByAuthor_Id(
                    authorId.get(),
                    pageRequest);
        } else {
            sounds = _soundRepo.findAll(pageRequest);
        }

        Page<SoundViewModel> soundViewModels = sounds.map(SoundViewModel::new);

        return new ResponseEntity<>(new Response(soundViewModels), null, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/put-sound")
    public ResponseEntity<?> putSound(
            Authentication auth,
            @ModelAttribute("sound") SoundUploadViewModel viewModel) {

        Map<String, String> errors = new HashMap<>();
        for (var violation : _validator.validate(viewModel))
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());

        User user = (User)auth.getPrincipal();
        if (user == null)
            errors.put("user", "error.invalid_user");

        if (!_soundStorageService.isAcceptedContentType(viewModel.getFile().getContentType()))
            errors.put("file", "error.unaccepted_content_type");

        if (!_imageStorageService.isAcceptedContentType(viewModel.getCover().getContentType()))
            errors.put("cover", "error.unaccepted_content_type");

        Optional<Genre> optionalGenre = _genreRepo.findById(viewModel.getGenreId());
        if (optionalGenre.isEmpty())
            errors.put("genreId", "error.invalid_genre");

        Sound sound = new Sound(
                viewModel.getTitle(),
                viewModel.getDescription(),
                optionalGenre.orElse(null),
                user);

        // TODO: move this into constructor?
        sound.setMimeType(viewModel.getFile().getContentType());

        if (!updateCover(new Image(), viewModel.getCover(), sound))
            errors.put("cover", "error.invalid_cover");

        if (!errors.isEmpty())
            return getErrorResponse(errors);

        try {
            sound = _soundRepo.save(sound);
            _soundStorageService.save(sound, viewModel.getFile().getInputStream());
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return getProfileRedirect(user);
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/bump-sound")
    public ResponseEntity<?> bumpSound(Authentication auth, @RequestParam("id") Long id) {
        if (auth == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Sound sound = optionalSound.get();
        sound.setLastBumpDate(new Date());

        try {
            _soundRepo.save(sound);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/patch-sound")
    public ResponseEntity<?> patchSound(
            Authentication auth,
            @RequestParam("id") Long id,
            @ModelAttribute("sound") SoundEditViewModel viewModel) {

        Map<String, String> errors = new HashMap<>();
        for (var violation : _validator.validate(viewModel))
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());

        User user = (User)auth.getPrincipal();
        if (user == null)
            errors.put("user", "error.invalid_user");

        Sound sound = tryGetSound(id, user);
        if (sound == null)
            errors.put("user", "error.not_an_author");
        else {
            sound.setTitle(viewModel.getTitle());
            sound.setDescription(viewModel.getDescription());

            if (viewModel.getGenreId() != sound.getGenre().getId()) {
                Optional<Genre> optionalGenre = _genreRepo.findById(viewModel.getGenreId());
                if (optionalGenre.isEmpty())
                    errors.put("genre", "error.invalid_genre");
                else
                    sound.setGenre(optionalGenre.get());
            }

            if (!viewModel.getCover().isEmpty() && !updateCover(sound.getCover(), viewModel.getCover(), sound))
                errors.put("cover", "error.invalid_cover");
        }

        if (!errors.isEmpty())
            return getErrorResponse(errors);

        try {
            _soundRepo.save(sound);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return getProfileRedirect(user);
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/delete-sound")
    public ResponseEntity<?> deleteSound(Authentication auth, @RequestParam("id") Long id) {
        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Sound sound = tryGetSound(id, user);
        if (sound == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        _soundStorageService.delete(sound);
        _soundRepo.deleteById(id);

        return getProfileRedirect(user);
    }

    private boolean updateCover(Image cover, MultipartFile file, Sound sound) {
        cover.setMimeType(file.getContentType());
        if (!_imageStorageService.isAcceptedContentType(cover.getMimeType()))
            return false;

        try {
            cover = _imageRepo.save(cover);
            _imageStorageService.save(cover, file.getInputStream());
            cover = _imageRepo.save(cover);
            sound.setCover(cover);
        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    private Sound tryGetSound(Long id, User author) {
        Optional<Sound> optionalSound = _soundRepo.findById(id);
        if (optionalSound.isEmpty())
            return null;

        Sound sound = optionalSound.get();
        if (!Objects.equals(sound.getAuthor().getId(), author.getId()))
            return null;

        return sound;
    }
}
