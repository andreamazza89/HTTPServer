package com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Resource;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Response;

import java.net.URI;

public class RedirectedResource implements Resource {

    private final URI uri;
    private final URI redirectLocation;

    public RedirectedResource(URI uri, URI redirectLocation) {
        this.uri = uri;
        this.redirectLocation = redirectLocation;
    }

    @Override
    public byte[] generateResponse(Request request) {
        String response = Response.STATUS_THREE_OH_TWO + "Location: " + redirectLocation + Response.END_OF_HEADERS;
        return response.getBytes();
    }

    @Override
    public URI uri() {
        return uri;
    }
}
