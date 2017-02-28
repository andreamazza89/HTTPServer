package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.BasicAuthenticator;
import com.andreamazzarella.http_server.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Resources {

    private final BasicAuthenticator authenticator;
    private List<Resource> resources = new ArrayList<>();
    private final List<Resource> resourcesToAuthenticate = new ArrayList<>();

    public Resources(BasicAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void requireAuthenticationFor(Resource resource) {
        resourcesToAuthenticate.add(resource);
    }

    public Resource findResource(Request request) {
        Optional<Resource> resourceFound = resources.stream()
                .filter((resource) -> resource.uri().getPath().equals(request.getUri().getPath()))
                .findFirst();

        if (!resourceFound.isPresent()) {
           return new MissingResource();
        }

        if (resourcesToAuthenticate.contains(resourceFound.get()) && !authenticator.isRequestValid(request)) {
            return new UnauthorisedResource();
        } else {
            return resourceFound.get();
        }
    }
}
