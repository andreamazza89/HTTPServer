package com.andreamazzarella.http_server;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class FileSystem {

    private final URI resourcesBasePath;

    public FileSystem(URI resourcesPath) {
        this.resourcesBasePath = resourcesPath;
    }

    public byte[] getResource(URI uri, DataRange dataRange) {
        File resource = retrieveResource(uri);
        int dataStart = dataRange.getStart((int)resource.length());
        int dataEnd = dataRange.getEnd((int)resource.length()) + 1;

        try {
            byte[] allResourceContent = Files.readAllBytes(resource.toPath());
            return Arrays.copyOfRange(allResourceContent, dataStart, dataEnd);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String getResourceContentType(URI uri) {
        File resource = retrieveResource(uri);
        return URLConnection.guessContentTypeFromName(resource.getName());
    }

    public void addOrReplaceResource(URI uri, byte[] resourceContent) {
        File resource = retrieveResource(uri);
        try {
            resource.delete();
            Files.write(resource.toPath(), resourceContent, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendContent(URI uri, byte[] resourceContent) {
        File resource = retrieveResource(uri);
        if (resource.exists()) {
            try {
                Files.write(resource.toPath(), resourceContent, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            addOrReplaceResource(uri, resourceContent);
        }
    }

    public void deleteResource(URI uri) {
        File resource = retrieveResource(uri);
        resource.delete();
    }

    private File retrieveResource(URI uri) {
        return new File(resourcesBasePath.getPath(), uri.getPath());
    }

    public boolean resourceDoesNotExist(URI uri) {
        File file = retrieveResource(uri);
        return !file.exists();
    }
}
