package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Header;
import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

import static com.andreamazzarella.http_server.Header.REDIRECT_HEADER_NAME;
import static com.andreamazzarella.http_server.Response.StatusCode._302;

public class RedirectController extends BaseController {

    protected Response get(Request request) {
        Header redirectHeader = new Header(REDIRECT_HEADER_NAME, "http://localhost:5000/");
        return new Response(_302).addHeader(redirectHeader);
    }
}
