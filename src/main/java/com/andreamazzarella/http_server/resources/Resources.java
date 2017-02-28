package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.BasicAuthenticator;
import com.andreamazzarella.http_server.Request;

import java.util.ArrayList;
import java.util.List;

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
        if (resourcePathDoesNotExist(request)) {
            return new MissingResource();
        }

        if (resourceMethodDoesNotExist(request)) {
            return new MethodNotAllowedResource();
        }

        Resource resourceFound = resources.stream()
                .filter((resource -> resource.uri().getPath().equals(request.uri().getPath()) && resource.method() == request.method()))
                .findFirst().get();

        if (resourcesToAuthenticate.contains(resourceFound) && !authenticator.isRequestValid(request)) {
            return new UnauthorisedResource();
        } else {
            return resourceFound;
        }
    }

    private boolean resourceMethodDoesNotExist(Request request) {
        return resources.stream()
                .noneMatch((resource -> resource.uri().getPath().equals(request.uri().getPath()) && resource.method() == request.method()));
    }

    private boolean resourcePathDoesNotExist(Request request) {
        return resources.stream()
                .noneMatch((resource) -> resource.uri().getPath().equals(request.uri().getPath()));
    }
}
