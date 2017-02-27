package com.andreamazzarella.http_server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Resources {

    private final BasicAuthenticator authenticator;
    private List<Resource> resources = new ArrayList<>();
    private final List<Resource> authenticatedResources = new ArrayList<>();

    Resources(BasicAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    void addResource(Resource resource) {
        resources.add(resource);
    }


    void requireAuthenticationFor(Resource resource) {
        authenticatedResources.add(resource);
    }

    Resource findResource(Request request) {
        Optional<Resource> resourceFound = resources.stream().filter((resource) -> resource.uri().getPath().equals(request.uri().getPath())).findFirst();

        if (!resourceFound.isPresent()) {
            return new MissingResource();
        }

        if (authenticatedResources.contains(resourceFound.get())) {
            if (authenticator.isRequestAuthenticated(request)) {
                return resourceFound.get();
            } else {
                return new UnauthorisedResource();
            }
        } else {
            return resourceFound.get();
        }
    }
}
