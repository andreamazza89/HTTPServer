package com.andreamazzarella.http_server;

import java.net.URI;

public class TeaPotResource implements Resource {

    private final URI resourcePath;

    TeaPotResource(URI resourcePath) {
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
