package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import static com.andreamazzarella.http_server.Response.StatusCode._404;
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
    }

}
