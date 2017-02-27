package com.andreamazzarella.http_server;

import java.net.URI;

class MissingResource implements Resource {

    @Override
    public byte[] generateResponse(Request request) {
        return Response.NOT_FOUND_RESPONSE.getBytes();
    }

    @Override
    public URI uri() {
        return null;
    }
}
