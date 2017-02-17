package com.andreamazzarella;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;

public class HTTPServer {
    private final int portNumber;
    private final String publicDirectoryPath;

    HTTPServer(int portNumber, String publicDirectoryPath) {
        this.portNumber = portNumber;
        this.publicDirectoryPath = publicDirectoryPath;
    }

    void start() {
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                serverSocket.accept();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    public String getPublicDirectoryPath() {
        return publicDirectoryPath;
    }
}
