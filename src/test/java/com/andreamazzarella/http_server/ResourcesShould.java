package com.andreamazzarella.http_server;

import org.junit.Test;

import java.net.URI;

import static junit.framework.TestCase.assertEquals;

public class ResourcesShould {

    @Test
    public void provideAMissingRouteIfNoneMatchesTheGivenURI() {
        Resources resources = new Resources();

        Resource resource = resources.findResource(URI.create("i/am/not/a/real/path"));

        assertEquals(MissingResource.class, resource.getClass());
    }

    @Test
    public void provideTheRouteAssociatedToTheGivenURI() {
        Resources resources = new Resources();
        Resource root = new TeaPotResource(URI.create("/"));
        resources.addResource(root);
        
        Resource resource = resources.findResource(URI.create("/"));

        assertEquals(root, resource);
    }
}
