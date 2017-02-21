package com.andreamazzarella.http_server;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class GetResponseShould {

    @Test
    public void respondWith200OnlyWhenRouteExistsButResourceDoesNot() {
        Route route = new Route(URI.create("/my_resource"));
        Blaah blaah = new Blaah(route.uri());

        String response = GetResponse.respond(route, blaah);

        assertEquals("HTTP/1.1 200 OK\n\n", response);
    }
}
