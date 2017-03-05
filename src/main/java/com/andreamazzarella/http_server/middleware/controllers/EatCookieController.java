package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Header;
import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

import java.util.Optional;

import static com.andreamazzarella.http_server.Response.StatusCode._200;

public class EatCookieController extends BaseController {

    protected Response get(Request request) {
        Optional<Header> cookieData = request.getCookieHeader();
        String cookieValue = cookieData.isPresent() ? cookieData.get().getValue() : "";
        byte[] body = ("mmmm " + cookieValue).getBytes();
        return new Response(_200).setBody(body);
    }

}
