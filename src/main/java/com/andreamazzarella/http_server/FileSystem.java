package com.andreamazzarella.http_server;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystem {

    private final URI resourcesBasePath;

    public FileSystem(URI resourcesPath) {
        this.resourcesBasePath = resourcesPath;
    }

    public byte[] getResource(URI uri, String dataRange) {
        File resource = retrieveResource(uri);
        int dataStart = parseDataStart(resource, dataRange);
        int dataEnd = parseDataEnd(resource, dataRange);

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

    private int parseDataStart(File resource, String dataRange) {
        if (dataRange == null) {
            return 0;
        }

        Pattern pattern = Pattern.compile(".*=(?<startByte>\\d*)-(?<endByte>\\d*).*");
        Matcher matcher = pattern.matcher(dataRange);
        matcher.matches();
        if (matcher.group("startByte").equals("")) {
            return (int)resource.length() - Integer.parseInt(matcher.group("endByte"));
        } else {
           return Integer.parseInt(matcher.group("startByte"));
        }
    }

    private int parseDataEnd(File resource, String dataRange) {
        if (dataRange == null) {
            return (int)resource.length();
        }

        Pattern pattern = Pattern.compile(".*=(?<startByte>\\d*)-(?<endByte>\\d*).*");
        Matcher matcher = pattern.matcher(dataRange);
        matcher.matches();
        if (matcher.group("endByte").equals("") || matcher.group("startByte").equals("")) {
            return (int)resource.length();
        } else {
            return Integer.parseInt(matcher.group("endByte")) + 1;
        }
    }

    private File retrieveResource(URI uri) {
        return new File(resourcesBasePath.getPath(), uri.getPath());
    }

    public boolean resourceDoesNotExist(URI uri) {
        File file = retrieveResource(uri);
        return !file.exists();
    }
}
