package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class MethodNotAllowedResourceShould {

    @Test
    public void respondWithFourOhFive() {
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("PATCH " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource notAllowedResource = new MethodNotAllowedResource();

        assertEquals(Response.NOT_ALLOWED_RESPONSE, new String(notAllowedResource.generateResponse(request)));
    }
}
