package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.DynamicResourceWithCookie;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Resource;
import com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources.Response;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertArrayEquals;

public class DynamicResourceWithCookieShould {

    @Test
    public void respondWithContentAndSetCookieExampleOne() throws IOException {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_cookie_resource/");
        String queryParameter = "?type=cats";
        String resourceContent = "Eat";
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + queryParameter + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        Resource cookieResource = new DynamicResourceWithCookie(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        assertArrayEquals((Response.STATUS_TWO_HUNDRED + "Set-Cookie: type=cats\n" + Response.END_OF_HEADERS + resourceContent).getBytes(), cookieResource.generateResponse(request));
    }

    @Test
    public void respondWithContentAndSetCookieExampleTwo() throws IOException {
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        URI resourcePath = URI.create("/path_to_cookie_resource/");
        String queryParameter = "?type=lols";
        String resourceContent = "Eat";
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET " + resourcePath + queryParameter + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        Resource cookieResource = new DynamicResourceWithCookie(resourcePath, fileSystem, new Request.Method[] {Request.Method.GET});

        assertArrayEquals((Response.STATUS_TWO_HUNDRED + "Set-Cookie: type=lols\n" + Response.END_OF_HEADERS + resourceContent).getBytes(), cookieResource.generateResponse(request));
    }
}
