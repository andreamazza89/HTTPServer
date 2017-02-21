package com.andreamazzarella.http_server;

import org.junit.Test;

import java.net.URI;

import static junit.framework.TestCase.assertEquals;

public class RoutesShould {

    @Test
    public void provideAMissingRouteIfNoneMatchesTheGivenURI() {
        Routes routes = new Routes();

        Route route = routes.findRoute(URI.create("i/am/not/a/real/path"));

        assertEquals(MissingRoute.class, route.getClass());
    }

    @Test
    public void provideTheRouteAssociatedToTheGivenURI() {
        Routes routes = new Routes();
        Route root = new Route(URI.create("/"));
        root.allowMethods(new Request.Method[] {Request.Method.GET});
        routes.addRoute(root);
        
        Route route = routes.findRoute(URI.create("/"));

        assertEquals(root, route);
    }
}
