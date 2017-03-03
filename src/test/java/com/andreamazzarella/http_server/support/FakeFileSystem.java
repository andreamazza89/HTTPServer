package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.ArrayOperations;
import com.andreamazzarella.http_server.FileSystem;

import java.net.URI;
import java.util.Optional;

public class FakeFileSystem extends FileSystem {

    private Optional<byte[]> resourceContent = Optional.empty();
    private String contentType;
    private boolean resourceExists = false;

    public FakeFileSystem(URI resourcesPath) {
        super(resourcesPath);
    }

    public void setContentTypeTo(URI uri, String contentType) {
        this.contentType = contentType;
    }

    public void setResourceExistsFlagTo(boolean resourceExists) {
        this.resourceExists = resourceExists;
    }

    @Override
    public Optional<byte[]> getResource(URI uri, String dataRange) {
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
    public void appendContent(URI uri, byte[] resourceContent) {
        this.resourceContent = Optional.of(ArrayOperations.concatenateData(this.resourceContent.orElse("".getBytes()), resourceContent));
    }

    @Override
    public void deleteResource(URI uri) {
        this.resourceContent = Optional.empty();
    }

    @Override
    public boolean doesResourceExist(URI uri) {
        return resourceExists;
    }
}
