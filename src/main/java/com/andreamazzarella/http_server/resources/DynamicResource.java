package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.Request;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DynamicResource implements Resource {

    private List<Request.Method> methodsAllowed;
    private URI uri;
    private FileSystem fileSystem;

    public DynamicResource(URI DynamicResourcePath, FileSystem fileSystem, Request.Method[] methodsAllowed) {
        this.uri = DynamicResourcePath;
        this.fileSystem = fileSystem;
        this.methodsAllowed = Arrays.asList(methodsAllowed);
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public Request.Method method() {
        return Request.Method.GET;
    }

    @Override
    public byte[] generateResponse(Request request) {
        if (!methodsAllowed.contains(request.method())) {
            return Response.NOT_ALLOWED_RESPONSE.getBytes();
        }

        switch (request.method()) {
            case GET:
                String dataRange = request.getHeader("Range");
                byte[] statusLine;
                byte[] resourceContent = fileSystem.getResource(request.uri(), dataRange).orElse("".getBytes());
                byte[] contentTypeHeader = generateContentTypeHeader();
                byte[] endOfHeaders = Response.END_OF_HEADERS.getBytes();
                byte[] parameters = serialiseParameters(request);

                if (dataRange == null) {
                    statusLine = (Response.STATUS_TWO_HUNDRED).getBytes();
                } else {
                    statusLine = (Response.STATUS_TWO_OH_SIX).getBytes();
                }
                return concatenateData(statusLine, contentTypeHeader, endOfHeaders, resourceContent, parameters);
            case HEAD:
                return (Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes();
            case POST:
                fileSystem.addOrReplaceResource(request.uri(), request.body().getBytes());
                return (Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes();
            case PUT:
                fileSystem.addOrReplaceResource(request.uri(), request.body().getBytes());
                return (Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes();
            case PATCH:
                fileSystem.addOrReplaceResource(request.uri(), request.body().getBytes());
                return (Response.STATUS_TWO_OH_FOUR + Response.END_OF_HEADERS).getBytes();
            case DELETE:
                fileSystem.deleteResource(request.uri());
                return (Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes();
            case OPTIONS:
                return (Response.STATUS_TWO_HUNDRED + generateAllowedMethodsHeader() + Response.END_OF_HEADERS).getBytes();
            default:
                throw new RuntimeException("Unhandled method");
        }
    }

    private byte[] serialiseParameters(Request request) {
        String serialisedParameters = "";
        Set<String> parameterKeys = request.getParams().keySet();
        for (String parameterKey : parameterKeys) {
            serialisedParameters += String.format("%s = %s\n", parameterKey, request.getParams().get(parameterKey));
        }
        return serialisedParameters.trim().getBytes();
    }
    private String generateAllowedMethodsHeader() {
        String allowedMethodsHeader = "Allow: ";
        for (Request.Method method : methodsAllowed) {
            allowedMethodsHeader += method.toString() + ",";
        }
        allowedMethodsHeader = allowedMethodsHeader.substring(0, allowedMethodsHeader.length()-1) + "\n";
        return allowedMethodsHeader;
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

    private byte[] generateContentTypeHeader() {
        String mediaType = fileSystem.getResourceContentType(uri);
        if (mediaType == null) {
            return new byte[0];
        } else {
            String contentTypeHeader = Response.CONTENT_TYPE_HEADER_NAME + mediaType + Response.NEWLINE;
            return contentTypeHeader.getBytes();
        }
    }
}
