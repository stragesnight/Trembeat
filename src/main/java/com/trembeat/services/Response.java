package com.trembeat.services;

import lombok.*;

/**
 * API Service response entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private Object responseObject;

    private String redirectURL;


    public Response(Object responseObject) {
        this.responseObject = responseObject;
        this.redirectURL = "";
    }
}
