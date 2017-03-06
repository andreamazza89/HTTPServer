package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.DataRange;
import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.Header;
import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request.Request;

import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.andreamazzarella.http_server.Response.StatusCode.*;

public class StaticAssetsController implements MiddleWare {

    private final FileSystem staticFileSystem;

    public StaticAssetsController(FileSystem staticFileSystem) {
        this.staticFileSystem = staticFileSystem;
    }

    @Override
    public Response generateResponseFor(Request request) {
        URI resourcePath = request.getUri();

        if (staticFileSystem.resourceDoesNotExist(resourcePath)) {
            return new Response(_404);
        }

        switch (request.getMethod()) {
            case GET:
                Header contentType = new Header(Header.CONTENT_TYPE_HEADER_NAME, staticFileSystem.getResourceContentType(resourcePath));
                Optional<Header> partialContentHeader = request.getRangeHeader();
                byte[] fullResourceContent = staticFileSystem.getResource(resourcePath, new DataRange());

                if (partialContentHeader.isPresent()) {
                    DataRange dataRange = DataRange.parseFromString(partialContentHeader.get());
                    byte[] partialResourceContent = staticFileSystem.getResource(resourcePath, dataRange);
                    return new Response(_206).addHeader(contentType).setBody(partialResourceContent);
                } else {
                    return new Response(_200).addHeader(contentType).setBody(fullResourceContent);
                }

            case PATCH:
                if (eTagMatchesResourceContent(request, resourcePath)) {
                    staticFileSystem.addOrReplaceResource(resourcePath, request.getBody().get());
                    return new Response(_204);
                } else {
                    return new Response(_412);
                }
            default:
                return new Response(_405);
        }
    }

    private boolean eTagMatchesResourceContent(Request request, URI resourcePath) {
        byte[] existingResourceContent = staticFileSystem.getResource(resourcePath, new DataRange());
        byte[] encodedExistingResourceContent = encodeContent(existingResourceContent);
        String encodedResourceContentInRequest = request.getIfMatchHeader().get().getValue();
        return convertToHexString(encodedExistingResourceContent).equalsIgnoreCase(encodedResourceContentInRequest);
    }

    private String convertToHexString(byte[] binary) {
        return DatatypeConverter.printHexBinary(binary);
    }

    private byte[] encodeContent(byte[] content) {
        try {
            return MessageDigest.getInstance("SHA-1").digest(content);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
