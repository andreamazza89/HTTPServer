package com.andreamazzarella.http_server;

import java.net.URI;

public class Route {

    private Request.Method[] methodsAllowed;
    private URI resourceToRedirect;
    private boolean isRedirectRoute;
    private URI resource;

    public Route(URI resource) {
        this.resource = resource;
    }

    public void allowMethods(Request.Method[] methods) {
        methodsAllowed = methods;
    }

    public Request.Method[] methodsAllowed() {
        return methodsAllowed;
    }

    public boolean isRedirectRoute() {
        return isRedirectRoute;
    }

    public void setRedirect(URI resourceToRedirect) {
        this.isRedirectRoute = true;
        this.resourceToRedirect = resourceToRedirect;
    }


    public URI redirectLocation() {
        return resourceToRedirect;
    }

    public URI getResource() {
        return resource;
    }
}
