package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;

public class DynamicResourceShould {

    @Test
    public void getContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        String resourceContent = "Content";
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        assertEquals(Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS + resourceContent, new String(dynamicResource.generateResponse(request)));
    }

    @Test
    public void patchContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/resource_path/");
        fileSystem.setContentTypeTo(resourcePath, "go ahead, patch me");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("PATCH " + resourcePath + " HTTP/1.1\nContent-Length: 23\n\n" + "here is a patch for you");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.PATCH});

        assertEquals(Response.STATUS_TWO_OH_FOUR + Response.END_OF_HEADERS, new String(dynamicResource.generateResponse(request)));
        assertEquals("here is a patch for you", new String(fileSystem.getResource(resourcePath, null).get()));
    }

    @Test
    public void includeContentTypeIfAvailable() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/resource_path");
        String resourceContent = "this one is a rich source of vitamin D";
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        fileSystem.setContentTypeTo(resourcePath, "image/jpeg");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        Resource staticResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        String expectedResponse = Response.STATUS_TWO_HUNDRED + "Content-Type: image/jpeg\n\n" + resourceContent;
        assertEquals(expectedResponse, new String(staticResource.generateResponse(request)));
    }

    @Test
    public void respondWith206WithGetPartialContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        String resourceContent = "Partial content";
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\nRange: bytes=0-14\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        assertEquals(Response.STATUS_TWO_OH_SIX + Response.END_OF_HEADERS + resourceContent, new String(dynamicResource.generateResponse(request)));
    }

    @Test
    public void onlyAllowConfiguredMethods() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("PATCH " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        assertEquals(Response.NOT_ALLOWED_RESPONSE, new String(dynamicResource.generateResponse(request)));
    }

    @Test
    public void respondToOptionsWithMethodsAllowedExampleOne() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem,
                new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});

        String expectedResponse = Response.STATUS_TWO_HUNDRED + "Allow: GET,OPTIONS\n" + Response.END_OF_HEADERS;
        assertEquals(expectedResponse, new String(dynamicResource.generateResponse(request)));
    }

    @Test
    public void respondToOptionsWithMethodsAllowedExampleTwo() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem,
                new Request.Method[] {Request.Method.POST, Request.Method.OPTIONS});

        String expectedResponse = Response.STATUS_TWO_HUNDRED + "Allow: POST,OPTIONS\n" + Response.END_OF_HEADERS;
        assertEquals(expectedResponse, new String(dynamicResource.generateResponse(request)));
    }

    @Test
    public void notIncludeResourceContentWithHead() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        fileSystem.addOrReplaceResource(resourcePath, "I am an elusive resource".getBytes());
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("HEAD " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.HEAD});

        assertEquals(Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS, new String(dynamicResource.generateResponse(request)));
    }

    @Test
    public void getEmptyContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        assertEquals(Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS, new String(dynamicResource.generateResponse(request)));
    }

    @Test
    public void postContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        String requestBody = "Freshly baked new content";
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("POST " + resourcePath + " HTTP/1.1\nContent-Length: " + requestBody.getBytes().length + "\n\n" + requestBody);
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.POST});

        assertEquals(Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS, new String(dynamicResource.generateResponse(request)));
        assertEquals(requestBody, new String(fileSystem.getResource(resourcePath, null).get()));
    }

    @Test
    public void putContent() {
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        String resourceContent = "Something has to change";
        String newResourceContent = "There, happy now?";
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("PUT " + resourcePath + " HTTP/1.1\nContent-Length: " + newResourceContent.getBytes().length + "\n\n" + newResourceContent);
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.PUT});

        assertEquals(Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS, new String(dynamicResource.generateResponse(request)));
        assertEquals(newResourceContent, new String(fileSystem.getResource(resourcePath, null).get()));
    }

    @Test
    public void deleteContent() {
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        String resourceContent = "Something has to disappear";
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("DELETE " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.DELETE});

        assertEquals(Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS, new String(dynamicResource.generateResponse(request)));
        assertEquals(Optional.empty(), fileSystem.getResource(resourcePath, null));
    }

    @Test
    public void includeRequestParametersInResponseBody() {
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + "?var1=lol&var2=cats" + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, new FakeFileSystem(URI.create("/double/path")), new Request.Method[] {Request.Method.GET});

        String expectedResponse = Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS + "var1 = lol\nvar2 = cats";
        assertEquals(expectedResponse, new String(dynamicResource.generateResponse(request)));
    }
}
