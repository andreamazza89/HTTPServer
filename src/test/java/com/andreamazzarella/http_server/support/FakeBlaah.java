package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.Blaah;

import java.net.URI;
import java.util.Optional;

public class FakeBlaah extends Blaah {

    private Optional<String> resourceContent;

    public FakeBlaah(URI resourcesPath) {
        super(resourcesPath);
    }

    public void setResourceContentTo(String resourceContent) {
        this.resourceContent = Optional.of(resourceContent);
    }

    @Override
    public Optional<String> getResource(URI uri) {
        return resourceContent;
    }

    @Override
    public void addResource(URI uri, String resourceContent) {
        this.resourceContent = Optional.of(resourceContent);
    }

    @Override
    public void deleteResource(URI uri) {
        this.resourceContent = Optional.empty();

    }
}
