package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.utilities.DataRange;
import com.andreamazzarella.http_server.request_response.Header;
import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request_response.Request;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.andreamazzarella.http_server.request_response.Header.CONTENT_LENGTH_HEADER_NAME;
import static com.andreamazzarella.http_server.request_response.Header.IF_MATCH_HEADER_NAME;
import static com.andreamazzarella.http_server.request_response.Header.RANGE_HEADER_NAME;
import static com.andreamazzarella.http_server.request_response.Response.StatusCode.*;
import static org.junit.Assert.assertEquals;

public class StaticAssetsControllerShould {

    @Test
    public void respondWithA404IfResourceDoesNotExist() {
        FakeFileSystem publicFileSystem = new FakeFileSystem(URI.create("./path_to_public_directory"));
        MiddleWare staticAssetsController = new StaticAssetsController(publicFileSystem);
        Request request = new Request("GET /made_up_resource HTTP/1.1", new ArrayList<>(), Optional.empty());

        Response response = staticAssetsController.generateResponseFor(request);

        assertEquals(_404, response.getStatusCode());
    }

    @Test
    public void respondWith405WithAnyMethodOtherThanGetOrPatch() {
        FakeFileSystem publicFileSystem = new FakeFileSystem(URI.create("./path_to_public_directory"));
        MiddleWare staticAssetsController = new StaticAssetsController(publicFileSystem);
        URI pathToStaticResource = URI.create("/path_to_static_resource");
        String resourceContent = "Planet Earth is full of resources!";
        publicFileSystem.addOrReplaceResource(pathToStaticResource, resourceContent.getBytes());
        Request request = new Request("POST " + pathToStaticResource + " HTTP/1.1", new ArrayList<>(), Optional.empty());

        Response response = staticAssetsController.generateResponseFor(request);

        assertEquals(_405, response.getStatusCode());
    }

    @Test
    public void includeResourceBodyAndContentTypeInTheResponse() {
        FakeFileSystem publicFileSystem = new FakeFileSystem(URI.create("./path_to_public_directory"));
        URI pathToStaticResource = URI.create("/path_to_static_resource");
        String resourceContent = "Planet Earth is full of resources!";
        publicFileSystem.addOrReplaceResource(pathToStaticResource, resourceContent.getBytes());
        publicFileSystem.setContentTypeTo(pathToStaticResource, "image/gif");
        MiddleWare staticAssetsController = new StaticAssetsController(publicFileSystem);
        Request request = new Request("GET " + pathToStaticResource + " HTTP/1.1", new ArrayList<>(), Optional.empty());

        Response response = staticAssetsController.generateResponseFor(request);

        assertEquals("image/gif", response.getHeaders().get(0).getValue());
        assertEquals(resourceContent, new String(response.getBody().get()));
        assertEquals(_200, response.getStatusCode());
    }

    @Test
    public void includePartialResourceBodyAndContentTypeInTheResponse() {
        FakeFileSystem publicFileSystem = new FakeFileSystem(URI.create("./path_to_public_directory"));
        URI pathToStaticResource = URI.create("/path_to_static_resource");
        String resourceContent = "Planet Earth is full of resources!";
        publicFileSystem.addOrReplaceResource(pathToStaticResource, resourceContent.getBytes());
        publicFileSystem.setContentTypeTo(pathToStaticResource, "text/plain");
        MiddleWare staticAssetsController = new StaticAssetsController(publicFileSystem);
        Header rangeHeader = new Header(RANGE_HEADER_NAME, "byte=0-5");
        List<Header> headers = new ArrayList<>();
        headers.add(rangeHeader);
        Request request = new Request("GET " + pathToStaticResource + " HTTP/1.1", headers, Optional.empty());

        Response response = staticAssetsController.generateResponseFor(request);

        assertEquals("text/plain", response.getHeaders().get(0).getValue());
        assertEquals("Planet", new String(response.getBody().get()));
        assertEquals(_206, response.getStatusCode());
    }

    @Test
    public void patchTheResourceIfEtagMatches() {
        FakeFileSystem publicFileSystem = new FakeFileSystem(URI.create("./path_to_public_directory"));
        URI pathToStaticResource = URI.create("/path_to_static_resource");
        String resourceContent = "Planet Earth is full of resources!";
        publicFileSystem.addOrReplaceResource(pathToStaticResource, resourceContent.getBytes());

        MiddleWare staticAssetsController = new StaticAssetsController(publicFileSystem);

        String encodedContent = DatatypeConverter.printHexBinary(encodeContent(resourceContent.getBytes()));

        Header ifMatch = new Header(IF_MATCH_HEADER_NAME, encodedContent);
        byte[] newContent = "Jumbotron will rule the world".getBytes();
        Header contentLength = new Header(CONTENT_LENGTH_HEADER_NAME, Integer.toString(newContent.length));
        List<Header> headers = new ArrayList<>();
        headers.add(ifMatch);
        headers.add(contentLength);
        Request request = new Request("PATCH " + pathToStaticResource + " HTTP/1.1", headers, Optional.of(newContent));

        Response response = staticAssetsController.generateResponseFor(request);

        assertEquals(_204, response.getStatusCode());
        assertEquals(new String(newContent), new String(publicFileSystem.getResource(pathToStaticResource, new DataRange())));
    }

    @Test
    public void notPatchTheResourceIfEtagDoesNotMatch() {
        FakeFileSystem publicFileSystem = new FakeFileSystem(URI.create("./path_to_public_directory"));
        URI pathToStaticResource = URI.create("/path_to_static_resource");
        String resourceContent = "Planet Earth is full of resources!";
        publicFileSystem.addOrReplaceResource(pathToStaticResource, resourceContent.getBytes());

        MiddleWare staticAssetsController = new StaticAssetsController(publicFileSystem);

        String encodedContent = DatatypeConverter.printHexBinary(encodeContent("wrong content".getBytes()));

        Header ifMatch = new Header(IF_MATCH_HEADER_NAME, encodedContent);
        byte[] newContent = "Jumbotron will not rule the world".getBytes();
        Header contentLength = new Header(CONTENT_LENGTH_HEADER_NAME, Integer.toString(newContent.length));
        List<Header> headers = new ArrayList<>();
        headers.add(ifMatch);
        headers.add(contentLength);
        Request request = new Request("PATCH " + pathToStaticResource + " HTTP/1.1", headers, Optional.of(newContent));

        Response response = staticAssetsController.generateResponseFor(request);

        assertEquals(_412, response.getStatusCode());
        assertEquals(new String(resourceContent), new String(publicFileSystem.getResource(pathToStaticResource, new DataRange())));
    }

    private byte[] encodeContent(byte[] content) {
        try {
            return MessageDigest.getInstance("SHA-1").digest(content);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
