package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.ImageRepository;
import com.trembeat.services.ImageStorageService;
import com.trembeat.services.MessageResolverService;
import com.trembeat.services.Response;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;

import java.util.Map;
import java.util.Optional;

/**
 * Generic content controller
 */
public abstract class GenericContentController {
    @Autowired
    protected ImageRepository _imageRepo;
    @Autowired
    protected ImageStorageService _imageStorageService;
    @Autowired
    private MessageResolverService _messageResolver;
    protected Validator _validator;


    public GenericContentController() {
        _validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    protected HttpHeaders getHeaders(FileEntity fileEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileEntity.getMimeType()));
        //headers.setContentLength(fileEntity.getContentLength());

        return headers;
    }

    protected ResponseEntity<Response> getProfileRedirect(User user) {
        return new ResponseEntity<>(
                new Response(null, String.format("/user/%d", user.getId())),
                null,
                HttpStatus.OK);
    }

    protected ResponseEntity<Response> getErrorResponse(Map<String, String> errors) {
        return new ResponseEntity<>(
                new Response(null, "", _messageResolver.resolveMessages(errors)),
                null,
                HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<?> getImageData(Long id) {
        Optional<Image> optionalImage = _imageRepo.findById(id);

        try {
            Image image = optionalImage.get();
            InputStreamResource isr = new InputStreamResource(_imageStorageService.load(image));
            return new ResponseEntity<>(isr, getHeaders(image), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
