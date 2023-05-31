package com.trembeat.controllers;

import com.trembeat.domain.models.FileEntity;
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
}
