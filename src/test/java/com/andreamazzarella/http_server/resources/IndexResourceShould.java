package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.support.FakeDirectoryExplorer;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class IndexResourceShould {

    @Test
    public void respondWithListOfStaticResourcesExampleOne() {
        FakeDirectoryExplorer directoryExplorer = new FakeDirectoryExplorer(URI.create("/double/path/"));
        String htmlListing = "all the answers to directory exploration lie within";
        directoryExplorer.setHTMLListingTo(htmlListing);
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1\n\n");
        Request request = Request.parseFromSocket(socketConnection);
        Resource indexResource = new IndexResource(URI.create("/"), directoryExplorer);

        String expectedResponse = Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS + htmlListing;
        assertEquals(expectedResponse, new String(indexResource.generateResponse(request)));
    }

    @Test
    public void respondWithListOfStaticResourcesExampleTwo() {
        FakeDirectoryExplorer directoryExplorer = new FakeDirectoryExplorer(URI.create("/double/path/"));
        String htmlListing = "may the force be with you";
        directoryExplorer.setHTMLListingTo(htmlListing);
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1\n\n");
        Request request = Request.parseFromSocket(socketConnection);
        Resource indexResource = new IndexResource(URI.create("/"), directoryExplorer);

        String expectedResponse = Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS + htmlListing;
        assertEquals(expectedResponse, new String(indexResource.generateResponse(request)));
    }
}
