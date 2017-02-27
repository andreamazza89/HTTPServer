package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Resource;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Response;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.StaticResource;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertArrayEquals;

public class StaticResourceShould {

    @Test
    public void onlyAllowAGetRequest() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("POST /path_to_static_resource HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource staticResource = new StaticResource(URI.create("/path_to_static_resource"), new FileSystem(URI.create("./resources")));

        assertArrayEquals(Response.NOT_ALLOWED_RESPONSE.getBytes(), staticResource.generateResponse(request));
    }

    @Test
    public void respondWith206WithGetPartialContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/resource_path/");
        String resourceContent = "Partial content";
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        fileSystem.setContentTypeTo(resourcePath, "text/plain");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\nRange: bytes=0-14\n\n");
        Request request = new Request(socketConnection);
        Resource staticResource = new StaticResource(resourcePath, fileSystem);

        assertArrayEquals((Response.STATUS_TWO_OH_SIX + "Content-Type: text/plain\n" + Response.END_OF_HEADERS + resourceContent).getBytes(), staticResource.generateResponse(request));
    }

    @Test
    public void patchContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/resource_path/");
        fileSystem.setContentTypeTo(resourcePath, "go ahead, patch me");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("PATCH " + resourcePath + " HTTP/1.1\nContent-Length: 23\n\n" + "here is a patch for you");
        Request request = new Request(socketConnection);
        Resource staticResource = new StaticResource(resourcePath, fileSystem);

        assertArrayEquals((Response.STATUS_TWO_OH_FOUR + Response.END_OF_HEADERS).getBytes(), staticResource.generateResponse(request));
        assertArrayEquals("here is a patch for you".getBytes(), fileSystem.getResource(resourcePath, null).get());
    }

    @Test
    public void generateAnAppropriateResponseWhenRetrievingExampleOne() throws IOException {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        URI resourcePath = URI.create("resource_path");
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./resources"));
        fileSystem.addOrReplaceResource(resourcePath, "this one is a rich source of vitamin D".getBytes());
        fileSystem.setContentTypeTo(resourcePath, "image/jpg");

        Resource staticResource = new StaticResource(URI.create("/path_to_static_resource"), fileSystem);

        String expectedResponse = Response.STATUS_TWO_HUNDRED + "Content-Type: image/jpg\n\n" + "this one is a rich source of vitamin D";
        assertArrayEquals(expectedResponse.getBytes(), staticResource.generateResponse(request));
    }

    @Test
    public void generateAnAppropriateResponseWhenRetrievingExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        URI resourcePath = URI.create("resource_path");
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./resources"));
        fileSystem.addOrReplaceResource(resourcePath, "rarely do we see such a resourceful resource, research has shown".getBytes());
        fileSystem.setContentTypeTo(resourcePath, null);

        Resource staticResource = new StaticResource(URI.create("/path_to_static_resource"), fileSystem);

        String expectedResponse = Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS + "rarely do we see such a resourceful resource, research has shown";
        assertArrayEquals(expectedResponse.getBytes(), staticResource.generateResponse(request));
    }
}
