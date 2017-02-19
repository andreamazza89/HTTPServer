package com.andreamazzarella.http_server;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RequestShould {

    @Test
    public void extractTheAbsolutePathRequestURIFromASimpleRequest() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /path/to/resource HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals("/path/to/resource", request.extractRequestURI());
    }

    @Test
    public void extractTheAbsolutePathRequestURIFromAnotherSimpleRequest() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("POST /path/to/lol/resource HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals("/path/to/lol/resource", request.extractRequestURI());
    }

    @Test
    public void haveAStringRepresentationOfASimpleRequest() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /path/to/resource HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals("GET /path/to/resource HTTP/1.1\n\n", request.toString());
    }

    @Test
    public void haveAStringRepresentationOfAnotherSimpleRequest() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("POST /path/to/resource HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals("POST /path/to/resource HTTP/1.1\n\n", request.toString());
    }

    @Test
    public void extractTheGetMethod() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals(Request.Method.GET, request.method());
    }

    @Test
    public void extractThePostMethod() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("POST / HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals(Request.Method.POST, request.method());
    }

    @Test
    public void extractTheOptionsMethod() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS / HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals(Request.Method.OPTIONS, request.method());
    }

    @Test
    public void extractTheDeleteMethod() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("DELETE / HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals(Request.Method.DELETE, request.method());
    }

    @Test
    public void extractThePutMethod() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("PUT / HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals(Request.Method.PUT, request.method());
    }

    @Test
    public void extractTheHeadMethod() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("HEAD / HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals(Request.Method.HEAD, request.method());
    }
}
