package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.request.Request;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

class Logger {
    private final FileSystem fileSystem;
    private List<URI> following = new ArrayList<>();

    Logger(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    void follow(URI resourcePath) {
        this.following.add(resourcePath);
    }

    void log(Request request) {
        if (following.contains(request.getUri())) {
            String requestLine = request.getRequestLine();
            byte[] requestLineWithNewLine = ArrayOperations.concatenateData(requestLine.getBytes(), "\n".getBytes());
            fileSystem.appendContent(URI.create("/logs"), requestLineWithNewLine);
        }
    }
}
