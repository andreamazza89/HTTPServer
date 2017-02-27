package com.andreamazzarella.http_server;

import java.net.URI;

public class UnauthorisedResource implements Resource {

    @Override
    public byte[] generateResponse(Request request) {
        return (Response.UNAUTHORISED_RESPONSE).getBytes();
    }

    @Override
    public URI uri() {
        return null;
    }
}
