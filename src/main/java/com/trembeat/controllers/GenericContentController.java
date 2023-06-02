package com.trembeat.controllers;

import com.trembeat.domain.models.*;
import com.trembeat.domain.repository.ImageRepository;
import com.trembeat.services.ImageStorageService;
import com.trembeat.services.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;

import java.util.Optional;

/**
 * Generic content controller
 */
public abstract class GenericContentController {
    @Autowired
    protected ImageRepository _imageRepo;
    @Autowired
    protected ImageStorageService _imageStorageService;



    protected HttpHeaders getHeaders(FileEntity fileEntity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileEntity.getMimeType()));
        headers.setContentLength(fileEntity.getContentLength());

        return headers;
    }

    protected ResponseEntity<Response> getProfileRedirect(User user) {
        return new ResponseEntity<>(
                new Response(null, String.format("/user/%d", user.getId())),
                null,
                HttpStatus.OK);
    }

    protected ResponseEntity<?> getImageData(Long id) {
        Optional<Image> optionalImage = _imageRepo.findById(id);
        if (optionalImage.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        Image image = optionalImage.get();
        InputStreamResource isr = new InputStreamResource(_imageStorageService.load(image));

        return new ResponseEntity<>(isr, getHeaders(image), HttpStatus.OK);
    }
}
