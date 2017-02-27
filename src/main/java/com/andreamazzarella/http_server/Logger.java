package com.andreamazzarella.http_server;

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
        if (following.contains(request.uri())) {
            fileSystem.appendResource(URI.create("/logs"), ArrayOperations.concatenateData(request.getRequestLine().getBytes(), "\n".getBytes()));
        }
    }
}
