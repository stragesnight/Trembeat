package com.trembeat.services;

import lombok.*;

import java.util.*;

/**
 * API Service response entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private Object responseObject;

    private String redirectURL;

    private Map<String, String> errors;


    public Response(Object responseObject) {
        this(responseObject, "", new HashMap<>());
    }

    public Response(Object responseObject, String redirectURL) {
        this(responseObject, redirectURL, new HashMap<>());
    }
}
