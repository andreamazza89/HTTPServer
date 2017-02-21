package com.andreamazzarella.http_server;

import java.net.URI;

public class Route {

    private Request.Method[] methodsAllowed;
    private URI resourceToRedirect;
    private boolean isRedirectRoute;
    private URI uri;

    Route(URI resource) {
        this.uri = resource;
    }

    void allowMethods(Request.Method[] methods) {
        methodsAllowed = methods;
    }

    public Request.Method[] methodsAllowed() {
        return methodsAllowed;
    }

    boolean isRedirectRoute() {
        return isRedirectRoute;
    }

    void setRedirect(URI resourceToRedirect) {
        this.isRedirectRoute = true;
        this.resourceToRedirect = resourceToRedirect;
    }

    URI redirectLocation() {
        return resourceToRedirect;
    }

    public URI uri() {
        return uri;
    }
}
