package com.andreamazzarella.http_server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class HTTPServer {
    private final int portNumber;
    private final String publicDirectoryPath;

    HTTPServer(int portNumber, String publicDirectoryPath) {
        this.portNumber = portNumber;
        this.publicDirectoryPath = publicDirectoryPath;
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

        ////////////////////maybe move into main?//////////////////////////////////////////
        Routes routes = new Routes();
        routes.addRoute("/", new Request.Method[] {Request.Method.GET});
        routes.addRoute("/method_options", new Request.Method[] {Request.Method.GET, Request.Method.HEAD,
                Request.Method.POST, Request.Method.OPTIONS, Request.Method.PUT});
        routes.addRoute("/method_options2", new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});
        routes.addRoute("/form", new Request.Method[] {Request.Method.POST, Request.Method.PUT});
        routes.addRoute("/redirect", new Request.Method[] {Request.Method.GET});
        routes.setRedirect("/redirect", "http://localhost:5000/");
        ////////////////////////////////////////////////////////////////////////////////

        Router router = new Router(socketConnection, routes);
        router.respondToRequest();
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
