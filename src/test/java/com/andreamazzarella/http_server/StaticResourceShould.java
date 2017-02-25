package com.andreamazzarella.http_server;

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
    public void generateAnAppropriateResponseWhenRetrievingExampleOne() throws IOException {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        URI resourcePath = URI.create("resource_path");
        socketConnection.setRequestTo("GET " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create(".resources"));
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
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create(".resources"));
        fileSystem.addOrReplaceResource(resourcePath, "rarely do we see such a resourceful resource, research has shown".getBytes());
        fileSystem.setContentTypeTo(resourcePath, "image/gif");

        Resource staticResource = new StaticResource(URI.create("/path_to_static_resource"), fileSystem);

        String expectedResponse = Response.STATUS_TWO_HUNDRED + "Content-Type: image/gif\n\n" + "rarely do we see such a resourceful resource, research has shown";
        assertArrayEquals(expectedResponse.getBytes(), staticResource.generateResponse(request));
    }
}
