package com.andreamazzarella.http_server;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RouterShould {

    @Test
    public void respondWithA200ToASimpleGetSCAFFOLD_TEST() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1");
        Router router = new Router(socketConnection);

        router.respondToRequest();

        assertEquals("HTTP/1.1 200 OK\n\n", socketConnection.messageReceived());
    }

    @Test
    public void respondWith404WhenResourceDoesNotExist() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("HEAD /foobar HTTP/1.1");
        Router router = new Router(socketConnection);

        router.respondToRequest();

        assertEquals("HTTP/1.1 404 Not Found\n\n", socketConnection.messageReceived());
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleOne() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options HTTP/1.1");
        Router router = new Router(socketConnection);

        router.respondToRequest();

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,HEAD,POST,OPTIONS,PUT\n\n", socketConnection.messageReceived());
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options2 HTTP/1.1");
        Router router = new Router(socketConnection);

        router.respondToRequest();

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,OPTIONS\n\n", socketConnection.messageReceived());
    }

}
