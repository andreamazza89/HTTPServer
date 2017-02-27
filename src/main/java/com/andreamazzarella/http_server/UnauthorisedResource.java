package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

public class UnauthorisedResource implements Resource {
    @Override
    public byte[] generateResponse(Request request) {
        return (Response.UNAUTHORISED_RESPONSE).getBytes();
    }

    @Override
    public Optional<URI> uri() {
        return null;
    }
}
