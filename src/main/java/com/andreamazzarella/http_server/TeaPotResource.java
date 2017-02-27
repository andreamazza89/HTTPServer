package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

public class TeaPotResource implements Resource {

    private final Optional<URI> resourcePath;

    TeaPotResource(URI resourcePath) {
        this.resourcePath = Optional.of(resourcePath);
    }

    @Override
    public byte[] generateResponse(Request request) {
        return Response.TEA_POT_RESPONSE.getBytes();
    }

    @Override
    public Optional<URI> uri() {
        return resourcePath;
    }
}