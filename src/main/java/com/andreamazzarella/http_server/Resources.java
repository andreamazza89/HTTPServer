package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Resources {

    private List<Resource> resources = new ArrayList<>();

    void addResource(Resource resource) {
        resources.add(resource);
    }

    Resource findResource(URI uri) {
        Optional<Resource> routeFound = resources.stream().filter((resourcePath) -> resourcePath.uri().get().equals(uri)).findFirst();
        return routeFound.orElse(new MissingResource());
    }
}
