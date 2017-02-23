package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

public class DynamicResource implements Resource {

    private Request.Method[] methodsAllowed;
    private URI DynamicResourceToRedirect;
    private boolean isRedirectRoute;
    private Optional<URI> uri;
    private Blaah blaah;
    private boolean isTeaPot;
    private Optional<String> DynamicResourceContent = Optional.empty();
    private String responseHeaders = "";

    DynamicResource(URI DynamicResourcePath) {
        this.uri = Optional.of(DynamicResourcePath);
    }

    public DynamicResource(URI DynamicResourcePath, Blaah blaah) {
        this.uri = Optional.of(DynamicResourcePath);
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

    void setRedirect(URI DynamicResourceToRedirect) {
        this.isRedirectRoute = true;
        this.DynamicResourceToRedirect = DynamicResourceToRedirect;
    }

    URI redirectLocation() {
        return DynamicResourceToRedirect;
    }

    @Override
    public Optional<URI> uri() {
        return uri;
    }

    boolean isTeaPot() {
        return isTeaPot;
    }

    void setTeaPot() {
        isTeaPot = true;
    }

    String getContent() {
        return DynamicResourceContent.orElse("");
    }

    public void execute(Request request) {
        switch (request.method()) {
            case GET:
                if (uri().toString().equals("/")) {
                    ////////////////////////////////
                    ///////////////////////////////////
                    // "Andrea should implement this: if path is root then create links and shit"
                    //also needs testing
                } else {
                    this.DynamicResourceContent = blaah.getResource(uri.get());
                    // this.DynamicResourceContent += make request.parameters into suitable string;
                }
                break;
            case POST:
                blaah.addResource(request.uri(), request.body());
                break;
            case PUT:
                blaah.addResource(request.uri(), request.body());
                break;
            case DELETE:
                blaah.deleteResource(uri.get());
                break;
            case OPTIONS:
               String methodsAllowedHeader = "";
               for (Request.Method method : methodsAllowed) {
                   methodsAllowedHeader += method.toString() + ",";
               }
               responseHeaders += "Allow: " + methodsAllowedHeader.substring(0, methodsAllowedHeader.length() - 1) + "\n";
               break;
        }
    }

    @Override
    public String generateResponse(Request request) {
        return Response.STATUS_TWO_HUNDRED + "\n";
    }

    String getResponseHeaders() {
        return responseHeaders;
    }
}
