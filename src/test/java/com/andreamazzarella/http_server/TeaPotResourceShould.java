package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class TeaPotResourceShould {

    @Test
    public void respondWithATeaPotResponse() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /coffee HTTP/1.1\n\nI'm a teapot");
        Request request = new Request(socketConnection);
        Resource teaPot = new TeaPotResource(URI.create("/coffee"));

        assertEquals(Response.TEA_POT_RESPONSE, teaPot.generateResponse(request));
    }
}
