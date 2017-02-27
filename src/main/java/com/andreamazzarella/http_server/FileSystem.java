package com.andreamazzarella.http_server;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileSystem {

    private final URI resourcesBasePath;

    public FileSystem(URI resourcesPath) {
        this.resourcesBasePath = resourcesPath;
    }

    public Optional<byte[]> getResource(URI uri, String dataRange) {
        File resource = retrieveResource(uri);
        int dataStart = parseDataStart(resource, dataRange);
        int dataEnd = parseDataEnd(resource, dataRange);
        long resourceLength = resource.length();

        if (resource.exists() && resourceLength > 0) {
            try {
                byte[] allResourceContent = Files.readAllBytes(resource.toPath());
                return Optional.of(Arrays.copyOfRange(allResourceContent, dataStart, dataEnd));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            return Optional.empty();
        }
    }

    private int parseDataStart(File resource, String dataRange) {
        if (dataRange == null) {
            return 0;
        }

        Pattern pattern = Pattern.compile(".*=(\\d*)-(\\d*).*");
        Matcher matcher = pattern.matcher(dataRange);
        matcher.matches();
        if (matcher.group(1).equals("")) {
            return (int)resource.length() - Integer.parseInt(matcher.group(2));
        } else {
           return Integer.parseInt(matcher.group(1));
        }
    }

    private int parseDataEnd(File resource, String dataRange) {
        if (dataRange == null) {
            return (int)resource.length();
        }

        Pattern pattern = Pattern.compile(".*=(\\d*)-(\\d*).*");
        Matcher matcher = pattern.matcher(dataRange);
        matcher.matches();
        if (matcher.group(2).equals("") || matcher.group(1).equals("")) {
            return (int)resource.length();
        } else {
            return Integer.parseInt(matcher.group(2)) + 1;
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

    public void appendResource(URI uri, byte[] resourceContent) {
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

    private byte[] concatenateData(byte[]... dataChunks) {
        int totalDataLength = getTotalDataLength(dataChunks);
        byte[] result = new byte[totalDataLength];
        ByteBuffer dataBuffer = ByteBuffer.wrap(result);

        for (byte[] dataChunk : dataChunks) {
            dataBuffer.put(dataChunk);
        }

        return result;
    }

    private int getTotalDataLength(byte[][] dataChunks) {
        int dataLength = 0;
        for (byte[] dataChunk : dataChunks) {
            dataLength += dataChunk.length;
        }
        return dataLength;
    }
}
