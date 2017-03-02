package com.andreamazzarella.http_server.resources;


import com.andreamazzarella.http_server.request.Request;

import java.net.URI;

public class MissingResource implements Resource {

    @Override
    public byte[] generateResponse(Request request) {
        return Response.NOT_FOUND_RESPONSE.getBytes();
    }

    @Override
    public URI uri() {
        return null;
    }
}
