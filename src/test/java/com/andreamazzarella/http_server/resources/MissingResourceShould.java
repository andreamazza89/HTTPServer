package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MissingResourceShould {

    @Test
    public void respondWithAFourOhFour() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /missing_resource HTTP/1.1\n\n");
        Request request = Request.parseFromSocket(socketConnection);
        Resource missingResource = new MissingResource();

        assertEquals(Response.NOT_FOUND_RESPONSE, new String(missingResource.generateResponse(request)));
    }
}
