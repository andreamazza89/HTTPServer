package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;

import static junit.framework.TestCase.assertEquals;

public class RequestShould {

    @Test
    public void extractTheAbsolutePathRequestURIFromASimpleRequest() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /path/to/resource HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals(URI.create("/path/to/resource"), request.uri());
    }

    @Test
    public void extractTheAbsolutePathRequestURIFromAnotherSimpleRequest() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("POST /path/to/lol/resource HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals(URI.create("/path/to/lol/resource"), request.uri());
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

    @Test
    public void extractTheBodyIfPresentExampleOne() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        String requestBody = "I am a getContent??";
        socketConnection.setRequestTo("HEAD / HTTP/1.1\nContent-Length: " + requestBody.getBytes().length + "\n\n" + requestBody);
        Request request = new Request(socketConnection);

        assertEquals(requestBody, request.body());
    }

    @Test
    public void extractTheBodyIfPresentExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        String requestBody = "I am definitely a getContent!";
        socketConnection.setRequestTo("HEAD / HTTP/1.1\nContent-Length: " + requestBody.getBytes().length + "\n\n" + requestBody);
        Request request = new Request(socketConnection);

        assertEquals(requestBody, request.body());
    }

    @Test
    public void extractTheHeadersIfPresentExampleOne() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1\nField-Name: Field-value\n\n");
        Request request = new Request(socketConnection);

        assertEquals("Field-value", request.getHeader("Field-Name"));
    }

    @Test
    public void extractTheHeadersIfPresentExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1\nField-Name: different-value\n\n");
        Request request = new Request(socketConnection);

        assertEquals("different-value", request.getHeader("Field-Name"));
    }
}
