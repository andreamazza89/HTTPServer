package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

import static com.andreamazzarella.http_server.Response.StatusCode._200;

public class SimpleController extends BaseController {

    protected Response get(Request request) {
        return new Response(_200);
    }

}
