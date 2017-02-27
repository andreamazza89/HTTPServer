package com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Resource;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Response;

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
