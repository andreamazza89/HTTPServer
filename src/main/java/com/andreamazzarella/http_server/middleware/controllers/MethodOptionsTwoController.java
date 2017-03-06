package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Request;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode._200;

public class MethodOptionsTwoController extends BaseController {

    protected Response get(Request request) {
        return new Response(_200);
    }
}
