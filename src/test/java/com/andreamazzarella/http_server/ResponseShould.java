package com.andreamazzarella.http_server;

import org.junit.Test;

import static com.andreamazzarella.http_server.Response.StatusCode.*;
import static org.junit.Assert.assertEquals;

public class ResponseShould {

    @Test
    public void convertASimple200ToBytes() {
        Response response = new Response(_200);

        assertEquals("HTTP/1.1 200 OK\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertASimple401ToBytes() {
        Response response = new Response(_401);

        assertEquals("HTTP/1.1 401 Unauthorized\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertASimple404ToBytes() {
        Response response = new Response(_404);

        assertEquals("HTTP/1.1 404 Not Found\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertASimple418ToBytes() {
        Response response = new Response(_418);

        assertEquals("HTTP/1.1 418 I'm a teapot\n\n", new String(response.toByteArray()));
    }

}
