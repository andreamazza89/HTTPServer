package com.andreamazzarella.http_server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
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

        ////////////////////maybe move into main? yes please////////////////////////////
        Routes routes = new Routes();

        Route root = new Route(URI.create("/"));
        Route methodOptions = new Route(URI.create("/method_options"));
        Route methodOptionsTwo = new Route(URI.create("/method_options2"));
        Route form = new Route(URI.create("/form"));
        Route redirect = new Route(URI.create("/redirect"));

        root.allowMethods(new Request.Method[] {Request.Method.GET});
        methodOptions.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.HEAD,
                Request.Method.POST, Request.Method.OPTIONS, Request.Method.PUT});
        methodOptionsTwo.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});
        form.allowMethods(new Request.Method[] {Request.Method.POST, Request.Method.PUT});
        redirect.allowMethods(new Request.Method[] {Request.Method.GET});

        redirect.setRedirect(URI.create("http://localhost:5000/"));

        routes.addRoute(root);
        routes.addRoute(methodOptions);
        routes.addRoute(methodOptionsTwo);
        routes.addRoute(form);
        routes.addRoute(redirect);
        ////////////////////////////////////////////////////////////////////////////////

         Request request = new Request(socketConnection);
         Route route = routes.findRoute(request.uri());
         String response = ResponseGenerator.createResponse(request, route);
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
