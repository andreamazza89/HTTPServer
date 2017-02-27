package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Resource;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Response;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.TeaPotResource;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertArrayEquals;

public class TeaPotResourceShould {

    @Test
    public void respondWithATeaPotResponse() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /coffee HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource teaPot = new TeaPotResource(URI.create("/coffee"));

        assertArrayEquals(Response.TEA_POT_RESPONSE.getBytes(), teaPot.generateResponse(request));
    }
}
