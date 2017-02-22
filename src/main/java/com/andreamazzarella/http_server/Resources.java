package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Resources {

    private List<Resource> resources = new ArrayList<>();

    void addRoute(Resource resource) {
        resources.add(resource);
    }

    Resource findRoute(URI uri) {
        Optional<Resource> routeFound = resources.stream().filter((route) -> route.uri().equals(uri)).findFirst();
        return routeFound.orElse(new MissingResource(uri));
    }
}
