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
        if (request.method() != Request.Method.GET && request.method() != Request.Method.PATCH) {
            return Response.NOT_ALLOWED_RESPONSE.getBytes();
        }

        switch (request.method()) {
            case GET:
                String dataRange = request.getHeader("Range");
                byte[] resourceContent = filesystem.getResource(uri.get(), dataRange).get();
                byte[] statusLine;
                byte[] contentTypeHeader = generateContentTypeHeader();
                byte[] endOfHeaders = Response.END_OF_HEADERS.getBytes();

                if (dataRange == null) {
                    statusLine = Response.STATUS_TWO_HUNDRED.getBytes();
                } else {
                    statusLine = Response.STATUS_TWO_OH_SIX.getBytes();
                }
                return ArrayOperations.concatenateData(statusLine, contentTypeHeader, endOfHeaders, resourceContent);
            case PATCH:
                filesystem.addOrReplaceResource(request.uri(), request.body().getBytes());
                return (Response.STATUS_TWO_OH_FOUR + Response.END_OF_HEADERS).getBytes();
            default:
                throw new RuntimeException("Unhandled method");
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
}
