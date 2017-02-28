package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class TeaPotResourceShould {

    @Test
    public void respondWithATeaPotResponse() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /coffee HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource teaPot = new TeaPotResource(URI.create("/coffee"));

        assertEquals(Response.TEA_POT_RESPONSE, new String(teaPot.generateResponse(request)));
    }
}
