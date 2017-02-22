package com.andreamazzarella.http_server;

import java.io.*;
import java.net.URI;
import java.util.Optional;

public class Blaah {

    private final URI resourcesBasePath;

    public Blaah(URI resourcesPath) {
        this.resourcesBasePath = resourcesPath;
    }

    public Optional<String> getResource(URI uri) {
        File resource = new File(resourcesBasePath.getPath(), uri.getPath());

        if (resource.exists() && resource.length() > 0) {
            try {
                FileReader reader = new FileReader(resource);
                return Optional.of(new BufferedReader(reader).readLine());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            return Optional.empty();
        }
    }

    public void addResource(URI uri, String resourceContent) {
        File resource = new File(resourcesBasePath.getPath(), uri.getPath());
        try {
            String path = resource.getCanonicalPath();
            PrintWriter printWriter = new PrintWriter(path);
            printWriter.write(resourceContent);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteResource(URI uri) {
        File resource = new File(resourcesBasePath.getPath(), uri.getPath());
        resource.delete();
    }
}
