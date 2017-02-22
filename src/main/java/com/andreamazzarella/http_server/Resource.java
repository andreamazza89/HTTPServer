package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

public class Resource {

    private Request.Method[] methodsAllowed;
    private URI resourceToRedirect;
    private boolean isRedirectRoute;
    private URI uri;
    private Blaah blaah;
    private boolean isTeaPot;
    private Optional<String> resourceContent = Optional.empty();
    private String responseHeaders = "";

    Resource(URI resourcePath) {
        this.uri = resourcePath;
    }

    public Resource(URI resourcePath, Blaah blaah) {
        this.uri = resourcePath;
        this.blaah = blaah;
    }

    void allowMethods(Request.Method[] methods) {
        methodsAllowed = methods;
    }

    public Request.Method[] methodsAllowed() {
        return methodsAllowed;
    }

    boolean isRedirect() {
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

    boolean isTeaPot() {
        return isTeaPot;
    }

    void setTeaPot() {
        isTeaPot = true;
    }

    String getContent() {
        return resourceContent.orElse("");
    }

    void execute(Request request) {
        switch (request.method()) {
            case GET:
                if (uri().toString().equals("/")) {
                    ////////////////////////////////
                    ///////////////////////////////////
                    // "Andrea should implement this: if path is root then create links and shit"
                    //also needs testing
                } else {
                    this.resourceContent = blaah.getResource(uri);
                    // this.resourceContent += make request.parameters into suitable string;
                }
                break;
            case POST:
                blaah.addResource(request.uri(), request.body());
                break;
            case PUT:
                blaah.addResource(request.uri(), request.body());
                break;
            case DELETE:
                blaah.deleteResource(uri);
                break;
            case OPTIONS:
               String methodsAllowedHeader = "";
               for (Request.Method method : methodsAllowed) {
                   methodsAllowedHeader += method.toString() + ",";
               }
               responseHeaders += "Allow: " + methodsAllowedHeader.substring(0, methodsAllowedHeader.length() - 1);
               break;
        }
    }

    String getResponseHeaders() {
        return responseHeaders;
    }
}
