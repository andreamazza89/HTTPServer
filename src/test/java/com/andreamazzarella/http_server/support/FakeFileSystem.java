package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.FileSystem;

import java.net.URI;
import java.util.Optional;

public class FakeFileSystem extends FileSystem {

    private Optional<byte[]> resourceContent;
    private String contentType;

    public FakeFileSystem(URI resourcesPath) {
        super(resourcesPath);
    }

    public void setContentTypeTo(URI uri, String contentType) {
        this.contentType = contentType;
    }

    @Override
    public Optional<byte[]> getResource(URI uri) {
        return resourceContent;
    }

    @Override
    public String getResourceContentType(URI uri) {
        return contentType;
    }

    @Override
    public void addOrReplaceResource(URI uri, byte[] resourceContent) {
        this.resourceContent = Optional.of(resourceContent);
    }

    @Override
    public void deleteResource(URI uri) {
        this.resourceContent = Optional.empty();
    }
}
