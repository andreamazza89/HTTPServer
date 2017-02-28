package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.Request;

import java.net.URI;

public class PostDynamicResource implements Resource {

    private final URI uri;
    private final FileSystem fileSystem;

    public PostDynamicResource(URI uri, FileSystem fileSystem) {
        this.uri = uri;
        this.fileSystem = fileSystem;
    }

    @Override
    public byte[] generateResponse(Request request) {
        fileSystem.addOrReplaceResource(request.uri(), request.body().getBytes());
        return (Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes();
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public Request.Method method() {
        return Request.Method.POST;
    }
}
