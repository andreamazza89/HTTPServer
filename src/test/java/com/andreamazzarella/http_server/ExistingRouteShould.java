package com.andreamazzarella.http_server;

import org.junit.Test;

import java.net.URI;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class ExistingRouteShould {

    @Test
    public void reportOnWhichMethodsAreAllowed() {
        URI resource = URI.create("/");
        Route route = new Route(resource);
        route.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.POST});

        assertArrayEquals(new Request.Method[] {Request.Method.GET, Request.Method.POST}, route.methodsAllowed());
    }

    @Test
    public void knowIfARouteIsToBeRedirected() {
        URI resource = URI.create("/no_redirect");
        Route route = new Route(resource);

        assertEquals(false, route.isRedirectRoute());
    }

    @Test
    public void allowARouteToBeRedirected() {
        URI resource = URI.create("/redirect");
        Route route = new Route(resource);
        route.allowMethods(new Request.Method[] {Request.Method.GET});
        route.setRedirect(URI.create("/go/to/this/uri"));

        assertEquals(true, route.isRedirectRoute());
        assertEquals("/go/to/this/uri", route.redirectLocation().getPath());
    }
}
