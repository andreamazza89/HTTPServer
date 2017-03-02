package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.resources.Resource;
import com.andreamazzarella.http_server.resources.Resources;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class HTTPServer {
    private final int portNumber;
    private final Resources resources;
    private final Logger logger;

    HTTPServer(int portNumber, Resources resources, Logger logger) {
        this.portNumber = portNumber;
        this.resources = resources;
        this.logger = logger;
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
        Request request = Request.parseFromSocket(socketConnection);
        // Response response = middlewareStack.generateResponseFor(com.andreamazzarella.http_server.request);
        // socketConnection.sendResponse(response)
        logger.log(request);
        Resource resource = resources.findResource(request);
        byte[] response = resource.generateResponse(request);
        socketConnection.write(response);
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
