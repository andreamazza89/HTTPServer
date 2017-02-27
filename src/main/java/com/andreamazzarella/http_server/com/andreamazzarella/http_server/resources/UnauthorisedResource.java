package com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Resource;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Response;

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
