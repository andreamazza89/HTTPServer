package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class DynamicResourceShould {

    @Test
    public void getContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        String resourceContent = "Stale content";
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        assertArrayEquals((Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS + resourceContent).getBytes(), dynamicResource.generateResponse(request));
    }

    @Test
    public void onlyAllowConfiguredMethods() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("PATCH " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        assertArrayEquals((Response.NOT_ALLOWED_RESPONSE).getBytes(), dynamicResource.generateResponse(request));
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
        assertArrayEquals((expectedResponse).getBytes(), dynamicResource.generateResponse(request));
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
        assertArrayEquals((expectedResponse).getBytes(), dynamicResource.generateResponse(request));
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

        assertArrayEquals((Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes(), dynamicResource.generateResponse(request));
    }

    @Test
    public void getEmptyContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        assertArrayEquals((Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS + "").getBytes(), dynamicResource.generateResponse(request));
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

        assertArrayEquals((Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes(), dynamicResource.generateResponse(request));
        assertArrayEquals(requestBody.getBytes(), fileSystem.getDynamicResource(resourcePath).get());
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

        assertArrayEquals((Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes(), dynamicResource.generateResponse(request));
        assertArrayEquals(newResourceContent.getBytes(), fileSystem.getDynamicResource(resourcePath).get());
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

        assertArrayEquals((Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS).getBytes(), dynamicResource.generateResponse(request));
        assertEquals(Optional.empty(), fileSystem.getDynamicResource(resourcePath));
    }

    @Test
    public void includeRequestParametersInResponseBody() {
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + "?var1=lol&var2=cats" + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource dynamicResource = new DynamicResource(resourcePath, new FakeFileSystem(URI.create("/double/path")), new Request.Method[] {Request.Method.GET});

        assertArrayEquals((Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS + "var1 = lol\nvar2 = cats").getBytes(), dynamicResource.generateResponse(request));
    }
}
