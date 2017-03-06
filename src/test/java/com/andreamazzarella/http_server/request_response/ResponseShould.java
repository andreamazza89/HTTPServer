package com.andreamazzarella.http_server.request_response;

import com.andreamazzarella.http_server.request_response.Header;
import com.andreamazzarella.http_server.request_response.Response;
import org.junit.Test;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode.*;
import static org.junit.Assert.assertEquals;

public class ResponseShould {

    @Test
    public void convertA200WithHeadersAndBodyToBytes() {
        Header header = new Header(Header.CONTENT_TYPE_HEADER_NAME, "image/gif");
        Response response = new Response(_200).addHeader(header).setBody("I am a tropical priest".getBytes());

        assertEquals("HTTP/1.1 200 OK\nContent-Type: image/gif\n\nI am a tropical priest", new String(response.toByteArray()));
    }

    @Test
    public void convertA204ToBytes() {
        Response response = new Response(_204);

        assertEquals("HTTP/1.1 204 No Content\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertA206ToBytes() {
        Response response = new Response(_206);

        assertEquals("HTTP/1.1 206 Partial Content\n\n", new String(response.toByteArray()));
    }
    @Test
    public void convertA302ToBytes() {
        Response response = new Response(_302);

        assertEquals("HTTP/1.1 302 Found\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertA401ToBytes() {
        Response response = new Response(_401);

        assertEquals("HTTP/1.1 401 Unauthorized\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertA404ToBytes() {
        Response response = new Response(_404);

        assertEquals("HTTP/1.1 404 Not Found\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertA405ToBytes() {
        Response response = new Response(_405);

        assertEquals("HTTP/1.1 405 Not Allowed\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertA412ToBytes() {
        Response response = new Response(_412);

        assertEquals("HTTP/1.1 412 Precondition Failed\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertA418ToBytes() {
        Response response = new Response(_418);

        assertEquals("HTTP/1.1 418 I'm a teapot\n\n", new String(response.toByteArray()));
    }

}
