package com.andreamazzarella.http_server;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class HTTPServerShould {

    @Ignore
    @Test
    public void acceptAConnectionOnTheGivenPortNumber() throws IOException {
//        HTTPServer server = new HTTPServer(4242, "./lol/cats/path", new Resources());
//        Executors.newSingleThreadExecutor().submit(server::start);
//
//        Socket socket = new Socket("localhost", 4242);
//
//        socket.getOutputStream().write("\n".getBytes()); // This will fail if the connection is not alive
//
//        socket.close();
    }

    @Ignore
    @Test
    public void acceptMultipleConnectionsOnTheGivenPortNumber() throws IOException {
//        HTTPServer server = new HTTPServer(4242, "./lol/cats/path", new Resources());
//        Executors.newSingleThreadExecutor().submit(server::start);
//
//        Socket socketOne = new Socket("localhost", 4242);
//        Socket socketTwo = new Socket("localhost", 4242);
//
//        socketOne.getOutputStream().write("\n".getBytes()); // This will fail if the connection is not alive
//        socketTwo.getOutputStream().write("\n".getBytes()); // This will fail if the connection is not alive
//
//
//        socketOne.close();
//        socketTwo.close();
    }

    @Ignore
    @Test
    public void setTheDirectoryPathToTheOneProvided_SCAFFOLD_TEST() {
//        HTTPServer server = new HTTPServer(4242, "./lol/cats/path", new Resources());
//
//        assertEquals("./lol/cats/path", server.getPublicDirectoryPath());
    }
}
