package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

public class RedirectedResource implements Resource {

    private final URI uri;
    private final URI redirectLocation;

    RedirectedResource(URI uri, URI redirectLocation) {
        this.uri = uri;
        this.redirectLocation = redirectLocation;
    }

    @Override
    public byte[] generateResponse(Request request) {
        String response = Response.STATUS_THREE_OH_TWO + "Location: " + redirectLocation + Response.END_OF_HEADERS;
        return response.getBytes();
    }

    @Override
    public Optional<URI> uri() {
        return Optional.of(uri);
    }
}
