package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

import static com.andreamazzarella.http_server.Response.StatusCode._200;

public class MethodOptionsController extends BaseController {

    protected Response get(Request request) {
        return new Response(_200);
    }

    protected Response head(Request request) {
        return new Response(_200);
    }

    protected Response post(Request request) {
        return new Response(_200);
    }

    protected Response put(Request request) {
        return new Response(_200);
    }

}
