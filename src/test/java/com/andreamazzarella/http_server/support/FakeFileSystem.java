package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.ArrayOperations;
import com.andreamazzarella.http_server.DataRange;
import com.andreamazzarella.http_server.FileSystem;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FakeFileSystem extends FileSystem {

    private Map<URI, byte[]> resources = new HashMap<>();
    private String contentType;
    private boolean resourceExists = false;

    public FakeFileSystem(URI resourcesPath) {
        super(resourcesPath);
    }

    public void setContentTypeTo(URI uri, String contentType) {
        this.contentType = contentType;
    }

    @Override
    public byte[] getResource(URI uri, DataRange dataRange) {
        byte[] resource = resources.get(uri);

        return Arrays.copyOfRange(resource, dataRange.getStart(resource.length), dataRange.getEnd(resource.length) + 1);
    }

    @Override
    public String getResourceContentType(URI uri) {
        return contentType;
    }

    @Override
    public void addOrReplaceResource(URI uri, byte[] resourceContent) {
        this.resourceExists = true;
        this.resources.put(uri, resourceContent);
    }

    @Override
    public void appendContent(URI uri, byte[] resourceContent) {
        if (resourceExists) {
            byte[] newResource = ArrayOperations.concatenateData(resources.get(uri), resourceContent);
            resources.put(uri, newResource);
        } else {
            addOrReplaceResource(uri, resourceContent);
        }
    }

    @Override
    public void deleteResource(URI uri) {
        this.resourceExists = false;
    }

    @Override
    public boolean resourceDoesNotExist(URI uri) {
        return !resources.containsKey(uri);
    }
}
