package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request_response.Request;
import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.socket_connection.SocketConnection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class HTTPServer {
    private final int portNumber;
    private final MiddleWare middleWareStack;

    HTTPServer(int portNumber, MiddleWare middleWareStack) {
        this.portNumber = portNumber;
        this.middleWareStack = middleWareStack;
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
        Response response =  middleWareStack.generateResponseFor(request);
        socketConnection.write(response.toByteArray());
    }

    private void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
