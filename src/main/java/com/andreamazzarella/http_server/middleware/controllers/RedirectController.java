package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.request_response.Header;
import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Request;

import static com.andreamazzarella.http_server.request_response.Header.REDIRECT_HEADER_NAME;
import static com.andreamazzarella.http_server.request_response.Response.StatusCode._302;

public class RedirectController extends BaseController {

    protected Response get(Request request) {
        Header redirectHeader = new Header(REDIRECT_HEADER_NAME, "http://localhost:5000/");
        return new Response(_302).addHeader(redirectHeader);
    }
}
