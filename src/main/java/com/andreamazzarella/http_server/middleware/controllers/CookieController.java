package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.request_response.Header;
import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Parameter;
import com.andreamazzarella.http_server.request_response.Request;

import java.util.Optional;

import static com.andreamazzarella.http_server.request_response.Header.SET_COOKIE_HEADER_NAME;
import static com.andreamazzarella.http_server.request_response.Response.StatusCode._200;

public class CookieController extends BaseController {

    protected Response get(Request request) {
        Optional<Parameter> cookieData = request.getParameter("type");
        String cookieValue = cookieData.isPresent() ? cookieData.get().getValue() : "";
        Header setCookieHeader = new Header(SET_COOKIE_HEADER_NAME, cookieValue);
        return new Response(_200).addHeader(setCookieHeader).setBody("Eat".getBytes());
    }
}
