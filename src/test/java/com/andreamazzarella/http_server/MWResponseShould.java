package com.andreamazzarella.http_server;

import org.junit.Test;

import static com.andreamazzarella.http_server.MWResponse.StatusCode.*;
import static org.junit.Assert.assertEquals;

public class MWResponseShould {

    @Test
    public void convertASimple200ToBytes() {
        MWResponse response = new MWResponse(_200);

        assertEquals("HTTP/1.1 200 OK\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertASimple401ToBytes() {
        MWResponse response = new MWResponse(_401);

        assertEquals("HTTP/1.1 401 Unauthorized\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertASimple404ToBytes() {
        MWResponse response = new MWResponse(_404);

        assertEquals("HTTP/1.1 404 Not Found\n\n", new String(response.toByteArray()));
    }

    @Test
    public void convertASimple418ToBytes() {
        MWResponse response = new MWResponse(_418);

        assertEquals("HTTP/1.1 418 I'm a teapot\n\n", new String(response.toByteArray()));
    }

}
