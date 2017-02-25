package com.andreamazzarella.http_server;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class FileSystem {

    private final URI resourcesBasePath;

    public FileSystem(URI resourcesPath) {
        this.resourcesBasePath = resourcesPath;
    }

    public Optional<byte[]> getDynamicResource(URI uri) {
        File resource = retrieveResource(uri);
        long resourceLength = resource.length();

        if (resource.exists() && resourceLength > 0) {
            try {
                byte[] resourceContent = Files.readAllBytes(resource.toPath());
                return Optional.of(resourceContent);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            return Optional.empty();
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

    public void deleteResource(URI uri) {
        File resource = retrieveResource(uri);
        resource.delete();
    }

    private File retrieveResource(URI uri) {
        return new File(resourcesBasePath.getPath(), uri.getPath());
    }

}
