package com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;

import java.net.URI;

public class TeaPotResource implements Resource {

    private final URI resourcePath;

    public TeaPotResource(URI resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public byte[] generateResponse(Request request) {
        return Response.TEA_POT_RESPONSE.getBytes();
    }

    @Override
    public URI uri() {
        return resourcePath;
    }
}