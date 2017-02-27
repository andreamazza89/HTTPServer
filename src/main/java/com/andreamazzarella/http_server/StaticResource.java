package com.andreamazzarella.http_server;

import java.net.URI;

public class StaticResource implements Resource {

    private final FileSystem filesystem;
    private final URI uri;

    StaticResource(URI uri, FileSystem filesystem) {
        this.filesystem = filesystem;
        this.uri = uri;
    }

    @Override
    public byte[] generateResponse(Request request) {
        if (request.method() != Request.Method.GET && request.method() != Request.Method.PATCH) {
            return Response.NOT_ALLOWED_RESPONSE.getBytes();
        }

        switch (request.method()) {
            case GET:
                String dataRange = request.getHeader("Range");
                byte[] statusLine;
                byte[] resourceContent = filesystem.getResource(uri, dataRange).get();
                byte[] endOfHeaders = Response.END_OF_HEADERS.getBytes();
                byte[] contentTypeHeader = generateContentTypeHeader();

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
    public URI uri() {
        return uri;
    }

    private byte[] generateContentTypeHeader() {
        String mediaType = filesystem.getResourceContentType(uri);
        if (mediaType == null) {
            return new byte[0];
        } else {
            String contentTypeHeader = Response.CONTENT_TYPE_HEADER_NAME + mediaType + Response.NEWLINE;
            return contentTypeHeader.getBytes();
        }
    }
}
