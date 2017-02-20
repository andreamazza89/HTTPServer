package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static junit.framework.TestCase.assertEquals;

public class RoutesShould {

    private static final Routes routes = new Routes();

    @Before
    public void addRoutes() {
        Route root = new Route(URI.create("/"));
        Route methodOptions = new Route(URI.create("/method_options"));
        Route methodOptionsTwo = new Route(URI.create("/method_options2"));
        Route redirect = new Route(URI.create("/redirect"));

        root.allowMethods(new Request.Method[] {Request.Method.GET});
        methodOptions.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.HEAD,
                Request.Method.POST, Request.Method.OPTIONS, Request.Method.PUT});
        methodOptionsTwo.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});
        redirect.allowMethods(new Request.Method[] {Request.Method.GET});

        redirect.setRedirect(URI.create("http://localhost:5000/"));

        routes.addRoute(root);
        routes.addRoute(methodOptions);
        routes.addRoute(methodOptionsTwo);
        routes.addRoute(redirect);
    }

    @Test
    public void respondWithA200ToASimpleGet() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1");
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 200 OK\n\n", response);
    }

    @Test
    public void respondWith404WhenResourceDoesNotExist() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("HEAD /foobar HTTP/1.1");
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 404 Not Found\n\n", response);
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleOne() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options HTTP/1.1");
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,HEAD,POST,OPTIONS,PUT\n\n", response);
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options2 HTTP/1.1");
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,OPTIONS\n\n", response);
    }

    @Test
    public void provideLocationForRedirect() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /redirect HTTP/1.1");
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 302 Found\nLocation: http://localhost:5000/\n\n", response);
    }



}
