package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;

import java.net.URI;

public class MethodNotAllowedResource implements Resource {
    @Override
    public byte[] generateResponse(Request request) {
        return Response.NOT_ALLOWED_RESPONSE.getBytes();
    }

    @Override
    public URI uri() {
        return null;
    }

    @Override
    public Request.Method method() {
        return null;
    }
}
