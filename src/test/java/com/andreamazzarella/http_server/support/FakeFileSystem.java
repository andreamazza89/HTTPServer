package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.FileSystem;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Optional;

public class FakeFileSystem extends FileSystem {

    private Optional<byte[]> resourceContent = Optional.empty();
    private String contentType;

    public FakeFileSystem(URI resourcesPath) {
        super(resourcesPath);
    }

    public void setContentTypeTo(URI uri, String contentType) {
        this.contentType = contentType;
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
    public void appendResource(URI uri, byte[] resourceContent) {
        this.resourceContent = Optional.of(concatenateData(this.resourceContent.orElse("".getBytes()), resourceContent));
    }

    @Override
    public void deleteResource(URI uri) {
        this.resourceContent = Optional.empty();
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
