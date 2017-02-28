package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.Request;

import java.net.URI;

public class DeleteDynamicResource implements Resource {

    private final URI uri;
    private final FileSystem fileSystem;

    DeleteDynamicResource(URI uri, FileSystem fileSystem) {
        this.uri = uri;
        this.fileSystem = fileSystem;
    }

    @Override
    public byte[] generateResponse(Request request) {
        fileSystem.deleteResource(request.uri());
        return (Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes();
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public Request.Method method() {
        return Request.Method.DELETE;
    }
}
