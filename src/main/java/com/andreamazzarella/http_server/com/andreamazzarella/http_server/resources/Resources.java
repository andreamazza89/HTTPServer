package com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources;

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
        Resource resource = getResourceOrMissingResource(request);

        if (resourcesToAuthenticate.contains(resource) && !authenticator.isRequestValid(request)) {
            return new UnauthorisedResource();
        } else {
            return resource;
        }
    }

    private Resource getResourceOrMissingResource(Request request) {
        Optional<Resource> resourceFound = resources.stream().filter((resource) -> resource.uri().getPath().equals(request.uri().getPath())).findFirst();
        return resourceFound.orElse(new MissingResource());
    }
}