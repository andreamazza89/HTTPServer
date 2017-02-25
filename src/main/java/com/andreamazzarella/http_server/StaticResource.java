package com.andreamazzarella.http_server;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Optional;

public class StaticResource implements Resource {

    private final FileSystem filesystem;
    private final Optional<URI> uri;

    StaticResource(URI uri, FileSystem filesystem) {
        this.filesystem = filesystem;
        this.uri = Optional.of(uri);
    }

    @Override
    public byte[] generateResponse(Request request) {
        if (request.method() != Request.Method.GET) {
            return Response.NOT_ALLOWED_RESPONSE.getBytes();
        } else {
            byte[] statusLine = Response.STATUS_TWO_HUNDRED.getBytes();
            byte[] contentTypeHeader = generateContentTypeHeader();
            byte[] endOfHeaders = Response.END_OF_HEADERS.getBytes();
            byte[] resourceContent = filesystem.getDynamicResource(uri.get()).get();

            return concatenateData(statusLine, contentTypeHeader, endOfHeaders, resourceContent);
        }
    }

    @Override
    public Optional<URI> uri() {
        return uri;
    }

    private byte[] generateContentTypeHeader() {
        String mediaType = filesystem.getResourceContentType(uri.get());
        String contentTypeHeader = Response.CONTENT_TYPE_HEADER_NAME + mediaType + Response.NEWLINE;
        return contentTypeHeader.getBytes();
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
