package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.ArrayOperations;
import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Logger implements MiddleWare {

    private final MiddleWare nextLayer;
    private final FileSystem fileSystem;
    private final List<URI> following = new ArrayList<>();

    public Logger(MiddleWare nextLayer, FileSystem fileSystem) {
        this.nextLayer = nextLayer;
        this.fileSystem = fileSystem;
    }

    @Override
    public Response generateResponseFor(Request request) {
        log(request);
        return nextLayer.generateResponseFor(request);
    }

    void follow(URI resourcePath) {
        following.add(resourcePath);
    }

    private void log(Request request) {
        if (following.contains(request.getUri())) {
            String requestLine = request.getRequestLine();
            byte[] requestLineWithNewLine = ArrayOperations.concatenateData(requestLine.getBytes(), "\n".getBytes());
            fileSystem.appendContent(URI.create("/logs"), requestLineWithNewLine);
        }
    }
}
