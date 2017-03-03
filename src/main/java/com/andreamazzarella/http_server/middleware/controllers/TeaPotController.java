package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request.Request;

import static com.andreamazzarella.http_server.Response.StatusCode._418;

public class TeaPotController implements MiddleWare {

    @Override
    public Response generateResponseFor(Request request) {
        return new Response(_418).setBody("I'm a teapot");
    }

}
