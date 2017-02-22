package com.andreamazzarella.http_server;

import org.junit.Test;

import java.net.URI;

import static junit.framework.TestCase.assertEquals;

public class RoutesShould {

    @Test
    public void provideAMissingRouteIfNoneMatchesTheGivenURI() {
        Resources resources = new Resources();

        Resource resource = resources.findRoute(URI.create("i/am/not/a/real/path"));

        assertEquals(MissingResource.class, resource.getClass());
    }

    @Test
    public void provideTheRouteAssociatedToTheGivenURI() {
        Resources resources = new Resources();
        Resource root = new Resource(URI.create("/"));
        root.allowMethods(new Request.Method[] {Request.Method.GET});
        resources.addRoute(root);
        
        Resource resource = resources.findRoute(URI.create("/"));

        assertEquals(root, resource);
    }
}
