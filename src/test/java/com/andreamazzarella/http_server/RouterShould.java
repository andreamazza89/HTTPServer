package com.andreamazzarella.http_server;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RouterShould {

    private static final Routes routes = new Routes();

    @Before
    public void addRoutes() {
        routes.addRoute("/", new Request.Method[] {Request.Method.GET});
        routes.addRoute("/method_options", new Request.Method[] {Request.Method.GET, Request.Method.HEAD,
                Request.Method.POST, Request.Method.OPTIONS, Request.Method.PUT});
        routes.addRoute("/method_options2", new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});
        routes.addRoute("/redirect",  new Request.Method[] {Request.Method.GET});
        routes.setRedirect("/redirect", "http://localhost:5000/");
    }

    @Test
    public void respondWithA200ToASimpleGetSCAFFOLD_TEST() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1");
        Router router = new Router(socketConnection, routes);

        router.respondToRequest();

        assertEquals("HTTP/1.1 200 OK\n\n", socketConnection.messageReceived());
    }

    @Test
    public void respondWith404WhenResourceDoesNotExist() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("HEAD /foobar HTTP/1.1");
        Router router = new Router(socketConnection, routes);

        router.respondToRequest();

        assertEquals("HTTP/1.1 404 Not Found\n\n", socketConnection.messageReceived());
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleOne() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options HTTP/1.1");
        Router router = new Router(socketConnection, routes);

        router.respondToRequest();

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,HEAD,POST,OPTIONS,PUT\n\n", socketConnection.messageReceived());
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options2 HTTP/1.1");
        Router router = new Router(socketConnection, routes);

        router.respondToRequest();

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,OPTIONS\n\n", socketConnection.messageReceived());
    }

    @Test
    public void provideLocationForRedirect() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /redirect HTTP/1.1");
        Router router = new Router(socketConnection, routes);

        router.respondToRequest();

        assertEquals("HTTP/1.1 302 Found\nLocation: http://localhost:5000/\n\n", socketConnection.messageReceived());
    }

}
