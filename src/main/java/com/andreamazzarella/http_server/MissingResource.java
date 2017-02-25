package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

class MissingResource implements Resource {

    @Override
    public byte[] generateResponse(Request request) {
        return Response.NOT_FOUND_RESPONSE.getBytes();
    }

    @Override
    public Optional<URI> uri() {
        return Optional.empty();
    }
}
