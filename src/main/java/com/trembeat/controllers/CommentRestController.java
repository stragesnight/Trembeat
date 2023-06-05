package com.trembeat.controllers;

import com.trembeat.configuration.WebConfiguration;
import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.*;
import com.trembeat.domain.viewmodels.*;
import com.trembeat.services.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CommentRestController extends GenericContentController {
    @Autowired
    private CommentRepository _commentRepo;
    @Autowired
    private SoundRepository _soundRepo;


    @GetMapping("/api/get-comments")
    public ResponseEntity<?> getComments(
            @RequestParam("soundId") Long soundId,
            @RequestParam("page") Optional<Integer> page) {

        Optional<Sound> optionalSound = _soundRepo.findById(soundId);
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        Pageable pageable = PageRequest.of(page.orElse(0), WebConfiguration.PAGE_LEN, sort);
        Page<CommentViewModel> comments = _commentRepo.findAllBySound(optionalSound.get(), pageable)
                .map(CommentViewModel::new);

        return new ResponseEntity<>(new Response(comments), null, HttpStatus.OK);
    }

    @PostMapping("/api/put-comment")
    public ResponseEntity<?> putComment(
            Authentication auth,
            @ModelAttribute("comment") CommentCreateViewModel viewModel) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Sound> optionalSound = _soundRepo.findById(viewModel.getSoundId());
        if (optionalSound.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Comment comment = new Comment(viewModel.getText(), optionalSound.get(), user);

        try {
            _commentRepo.save(comment);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/delete-comment")
    public ResponseEntity<?> deleteComment(
            Authentication auth,
            @RequestParam("id") Long commentId) {

        User user = (User)auth.getPrincipal();
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Comment> optionalComment = _commentRepo.findById(commentId);
        if (optionalComment.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Comment comment = optionalComment.get();
        if (comment.getUser().getId() != user.getId())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        _commentRepo.delete(comment);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
