package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

class MissingResource implements Resource {

    MissingResource(URI uri) {
    }

    @Override
    public String generateResponse(Request request) {
        return "not implemented yet";
    }

    @Override
    public Optional<URI> uri() {
        return Optional.empty();
    }
}
