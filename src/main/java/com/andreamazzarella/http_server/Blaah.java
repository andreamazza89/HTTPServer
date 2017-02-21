package com.andreamazzarella.http_server;

import java.io.*;
import java.net.URI;
import java.util.Optional;

class Blaah {

    private final URI resourcesBasePath;

    Blaah(URI resourcesPath) {
        this.resourcesBasePath = resourcesPath;
    }

    Optional<String> getResource(URI uri) {
        File resource = new File(resourcesBasePath.getPath(), uri.getPath());

        if (resource.exists()) {
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

    void addResource(URI uri, String resourceContent) {
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
}
