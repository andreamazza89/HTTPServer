package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertArrayEquals;

public class RedirectResourceShould {

    @Test
    public void respondWithARedirectMessageExampleOne() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /redirect_this_please HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        URI redirectLocation = URI.create("/new/location/");
        Resource redirectedResource = new RedirectedResource(URI.create("/redirect_this_please"), redirectLocation);

        String expectedResponse = Response.STATUS_THREE_OH_TWO + "Location: " + redirectLocation + Response.END_OF_HEADERS;
        assertArrayEquals(expectedResponse.getBytes(), redirectedResource.generateResponse(request));
    }

    @Test
    public void respondWithARedirectMessageExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /redirect_this_please HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        URI redirectLocation = URI.create("/newer/location/");
        Resource redirectedResource = new RedirectedResource(URI.create("/redirect_this_please"), redirectLocation);

        String expectedResponse = Response.STATUS_THREE_OH_TWO + "Location: " + redirectLocation + Response.END_OF_HEADERS;
        assertArrayEquals(expectedResponse.getBytes(), redirectedResource.generateResponse(request));
    }
}
