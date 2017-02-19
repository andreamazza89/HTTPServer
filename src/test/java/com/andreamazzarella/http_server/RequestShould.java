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
}
