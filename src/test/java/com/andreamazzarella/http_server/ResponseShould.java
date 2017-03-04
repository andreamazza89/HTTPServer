package com.andreamazzarella.http_server;

import org.junit.Test;

import static com.andreamazzarella.http_server.Response.StatusCode.*;
import static org.junit.Assert.assertEquals;

public class ResponseShould {

    @Test
    public void convertA200WithHeadersAndBodyToBytes() {
        Header header = new Header(Header.CONTENT_TYPE_HEADER_NAME, "image/gif");
        Response response = new Response(_200).addHeader(header).setBody("I am a tropical priest".getBytes());

        assertEquals("HTTP/1.1 200 OK\nContent-Type: image/gif\n\nI am a tropical priest", new String(response.toByteArray()));
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
    public void convertASimple405ToBytes() {
        Response response = new Response(_405);

        assertEquals("HTTP/1.1 405 Not Allowed\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertASimple418ToBytes() {
        Response response = new Response(_418);

        assertEquals("HTTP/1.1 418 I'm a teapot\n\n", new String(response.toByteArray()));
    }

}
