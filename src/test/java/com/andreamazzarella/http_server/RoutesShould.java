package com.andreamazzarella.http_server;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class RoutesShould {

    @Test
    public void knowIfARouteDoesNotExist() {
        Routes routes = new Routes();

        assertEquals(false, routes.isRouteAvailable("/"));
    }

    @Test
    public void knowIfARouteExists() {
        Routes routes = new Routes();
        routes.addRoute("/", new Request.Method[] {Request.Method.GET, Request.Method.POST});

        assertEquals(true, routes.isRouteAvailable("/"));
    }

    @Test
    public void reportOnWhichMethodsAreAllowed() {
        Routes routes = new Routes();
        routes.addRoute("/", new Request.Method[] {Request.Method.GET, Request.Method.POST});

        assertArrayEquals(new Request.Method[] {Request.Method.GET, Request.Method.POST}, routes.methodsAllowed("/"));
    }
}
