package com.trembeat.controllers;

import com.trembeat.domain.models.FileEntity;
import com.trembeat.domain.models.User;
import com.trembeat.services.Response;
import jdk.jfr.ContentType;
import org.springframework.http.*;

/**
 * Generic content controller
 */
public abstract class GenericContentController {
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
}
