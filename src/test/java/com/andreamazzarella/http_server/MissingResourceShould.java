package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.resources.MissingResource;
import com.andreamazzarella.http_server.resources.Resource;
import com.andreamazzarella.http_server.resources.Response;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class MissingResourceShould {

    @Test
    public void respondWithAFourOhFour() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /missing_resource HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource missingResource = new MissingResource();

        assertArrayEquals(Response.NOT_FOUND_RESPONSE.getBytes(), missingResource.generateResponse(request));
    }
}
