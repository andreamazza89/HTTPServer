package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

public class DynamicResource implements Resource {

    private Request.Method[] methodsAllowed;
    private URI DynamicResourceToRedirect;
    private boolean isRedirectRoute;
    private Optional<URI> uri;
    private FileSystem fileSystem;
    private boolean isTeaPot;
    private Optional<String> DynamicResourceContent = Optional.empty();
    private String responseHeaders = "";

    DynamicResource(URI DynamicResourcePath) {
        this.uri = Optional.of(DynamicResourcePath);
    }

    public DynamicResource(URI DynamicResourcePath, FileSystem fileSystem) {
        this.uri = Optional.of(DynamicResourcePath);
        this.fileSystem = fileSystem;
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
    }

    @Override
    public byte[] generateResponse(Request request) {
        return "not implemented yet: DynamicResource".getBytes();
    }

    String getResponseHeaders() {
        return responseHeaders;
    }
}
