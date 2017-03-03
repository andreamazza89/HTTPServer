package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.Header;
import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request.Request;

import java.net.URI;

import static com.andreamazzarella.http_server.Response.StatusCode._200;
import static com.andreamazzarella.http_server.Response.StatusCode._404;

public class StaticAssetsController implements MiddleWare {

    private final FileSystem staticFileSystem;

    public StaticAssetsController(FileSystem staticFileSystem) {
        this.staticFileSystem = staticFileSystem;
    }

    @Override
    public Response generateResponseFor(Request request) {
        URI resourcePath = request.getUri();

        if (staticFileSystem.doesResourceExist(resourcePath)) {
            byte[] responseBody = staticFileSystem.getResource(resourcePath, null);
            Header contentType = new Header(Header.CONTENT_TYPE_HEADER_NAME, staticFileSystem.getResourceContentType(resourcePath));

            return new Response(_200).addHeader(contentType).setBody(responseBody);
        } else {
            return new Response(_404);
        }
    }

}
