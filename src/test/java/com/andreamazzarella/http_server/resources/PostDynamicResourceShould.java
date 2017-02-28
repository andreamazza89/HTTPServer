package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class PostDynamicResourceShould {

    @Test
    public void postContent() {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_resource/");
        String requestBody = "Freshly baked new content";
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("POST " + resourcePath + " HTTP/1.1\nContent-Length: " + requestBody.getBytes().length + "\n\n" + requestBody);
        Request request = new Request(socketConnection);
        Resource postDynamicResource = new PostDynamicResource(resourcePath, fileSystem);

        assertEquals(Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS, new String(postDynamicResource.generateResponse(request)));
        assertEquals(requestBody, new String(fileSystem.getResource(resourcePath, null).get()));
    }


}
