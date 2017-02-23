package com.andreamazzarella.http_server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class HTTPServer {
    private final int portNumber;
    private final String publicDirectoryPath;
    private final Resources resources;

    HTTPServer(int portNumber, String publicDirectoryPath, Resources resources) {
        this.portNumber = portNumber;
        this.publicDirectoryPath = publicDirectoryPath;
        this.resources = resources;
    }

    void start() {
        ExecutorService connectionsPool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket socket = serverSocket.accept();
                connectionsPool.submit(() -> {
                    respondToRequest(socket);
                    closeSocket(socket);
                });
            }
        } catch (IOException e) {
            connectionsPool.shutdownNow();
            throw new UncheckedIOException(e);
        }
    }

    private void respondToRequest(Socket socket) {
        SocketConnection socketConnection = new SocketConnection(socket);
        Request request = new Request(socketConnection);
        Resource resource = resources.findRoute(request.uri());
        String response = resource.generateResponse(request);
        socketConnection.write(response);
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    String getPublicDirectoryPath() {
        return publicDirectoryPath;
    }
}
