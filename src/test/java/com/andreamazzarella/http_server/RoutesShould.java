package com.andreamazzarella.http_server;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class RoutesShould {

    @Test
    public void knowIfARouteDoesNotExist() {
        Routes routes = new Routes();

        assertEquals(false, routes.doesRouteExist("/"));
    }

    @Test
    public void knowIfARouteExists() {
        Routes routes = new Routes();
        routes.addRoute("/", new Request.Method[] {Request.Method.GET, Request.Method.POST});

        assertEquals(true, routes.doesRouteExist("/"));
    }

    @Test
    public void reportOnWhichMethodsAreAllowed() {
        Routes routes = new Routes();
        routes.addRoute("/", new Request.Method[] {Request.Method.GET, Request.Method.POST});

        assertArrayEquals(new Request.Method[] {Request.Method.GET, Request.Method.POST}, routes.methodsAllowed("/"));
    }

    @Test
    public void knowIfARouteIsToBeRedirected() {
        Routes routes = new Routes();
        routes.addRoute("/no_redirect", new Request.Method[] {Request.Method.GET});

        assertEquals(false, routes.isRedirectRoute("/no_redirect"));
    }

    @Test
    public void allowARouteToBeRedirected() {
        Routes routes = new Routes();
        routes.addRoute("/redirect", new Request.Method[] {Request.Method.GET});
        routes.setRedirect("/redirect", "/go/to/this/uri");

        assertEquals(true, routes.isRedirectRoute("/redirect"));
        assertEquals("/go/to/this/uri", routes.redirectLocation("/redirect"));
    }
}
