package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

import java.util.Optional;

import static com.andreamazzarella.http_server.Response.StatusCode._200;

public class FormController extends BaseController{

    private Optional<byte[]> formContent = Optional.empty();

    protected Response get(Request request) {
        return new Response(_200).setBody(formContent.orElse("".getBytes()));
    }

    protected Response post(Request request) {
        formContent = request.getBody();
        return new Response(_200);
    }

    protected Response put(Request request) {
        return post(request);
    }

    protected Response delete(Request request) {
        formContent = Optional.empty();
        return new Response(_200);
    }
}
