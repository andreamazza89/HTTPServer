package com.andreamazzarella.http_server;

import java.net.URI;
import java.nio.ByteBuffer;
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
            fileSystem.appendResource(URI.create("/logs"), concatenateData(request.getRequestLine().getBytes(), "\n".getBytes()));
        }
    }

    private byte[] concatenateData(byte[]... dataChunks) {
        int totalDataLength = getTotalDataLength(dataChunks);
        byte[] result = new byte[totalDataLength];
        ByteBuffer dataBuffer = ByteBuffer.wrap(result);

        for (byte[] dataChunk : dataChunks) {
            dataBuffer.put(dataChunk);
        }

        return result;
    }

    private int getTotalDataLength(byte[][] dataChunks) {
        int dataLength = 0;
        for (byte[] dataChunk : dataChunks) {
            dataLength += dataChunk.length;
        }
        return dataLength;
    }
}
