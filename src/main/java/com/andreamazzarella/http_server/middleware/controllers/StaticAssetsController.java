package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.request_response.Header;
import com.andreamazzarella.http_server.request_response.Request;
import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.utilities.DataRange;
import com.andreamazzarella.http_server.utilities.FileSystem;

import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode.*;

public class StaticAssetsController extends BaseController {

    private final FileSystem staticFileSystem;

    public StaticAssetsController(FileSystem staticFileSystem) {
        this.staticFileSystem = staticFileSystem;
    }

    protected Response get(Request request) {
        URI resourcePath = request.getUri();
        if (staticFileSystem.resourceDoesNotExist(resourcePath)) return new Response(_404);

        Header contentType = new Header(Header.CONTENT_TYPE_HEADER_NAME, staticFileSystem.getResourceContentType(resourcePath));
        Optional<Header> partialContentHeader = request.getRangeHeader();

        if (partialContentHeader.isPresent()) {
            DataRange dataRange = DataRange.parseFromString(partialContentHeader.get());
            byte[] partialResourceContent = staticFileSystem.getResource(resourcePath, dataRange);
            return new Response(_206).addHeader(contentType).setBody(partialResourceContent);
        } else {
            byte[] fullResourceContent = staticFileSystem.getResource(resourcePath, new DataRange());
            return new Response(_200).addHeader(contentType).setBody(fullResourceContent);
        }
    }

    protected Response patch(Request request) {
        URI resourcePath = request.getUri();
        if (staticFileSystem.resourceDoesNotExist(resourcePath)) return new Response(_404);

        if (eTagMatchesResourceContent(request, resourcePath)) {
            staticFileSystem.addOrReplaceResource(resourcePath, request.getBody().get());
            return new Response(_204);
        } else {
            return new Response(_412);
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
