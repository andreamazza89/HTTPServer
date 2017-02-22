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
        Resources resources = new Resources();
        URI resourcesBasePath = URI.create("./resources/");
        Blaah fileSystem = new Blaah(resourcesBasePath);

        Resource root = new Resource(URI.create("/"), fileSystem);
        Resource methodOptions = new Resource(URI.create("/method_options"), fileSystem);
        Resource methodOptionsTwo = new Resource(URI.create("/method_options2"), fileSystem);
        Resource form = new Resource(URI.create("/form"), fileSystem);
        Resource redirect = new Resource(URI.create("/redirect"), fileSystem);
        Resource coffee = new Resource(URI.create("/coffee"), fileSystem);
        Resource tea = new Resource(URI.create("/tea"), fileSystem);

        root.allowMethods(new Request.Method[] {Request.Method.GET});
        methodOptions.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.HEAD,
                Request.Method.POST, Request.Method.OPTIONS, Request.Method.PUT});
        methodOptionsTwo.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});
        form.allowMethods(new Request.Method[] {Request.Method.POST, Request.Method.PUT});
        redirect.allowMethods(new Request.Method[] {Request.Method.GET});
        tea.allowMethods(new Request.Method[] {Request.Method.GET});

        redirect.setRedirect(URI.create("http://localhost:5000/"));
        coffee.setTeaPot();

        resources.addRoute(root);
        resources.addRoute(methodOptions);
        resources.addRoute(methodOptionsTwo);
        resources.addRoute(form);
        resources.addRoute(redirect);
        resources.addRoute(tea);
        resources.addRoute(coffee);
        ////////////////////////////////////////////////////////////////////////////////

         Request request = new Request(socketConnection);
         Resource resource = resources.findRoute(request.uri());
         String response = ResponseGenerator.createResponse(request, resource);
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
