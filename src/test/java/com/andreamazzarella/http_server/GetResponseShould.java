package com.andreamazzarella.http_server;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class GetResponseShould {

    @Test
    public void respondWith200OnlyWhenRouteExistsButResourceDoesNot() {
        DynamicResource resource = new DynamicResource(URI.create("/my_resource"));
        Blaah blaah = new Blaah(resource.uri().get());

        String response = GetResponse.respond(resource, blaah);

        assertEquals("HTTP/1.1 200 OK\n\n", response);
    }
}
