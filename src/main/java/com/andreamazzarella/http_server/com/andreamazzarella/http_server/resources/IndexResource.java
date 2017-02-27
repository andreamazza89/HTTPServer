package com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.DirectoryExplorer;
import com.andreamazzarella.http_server.Request;

import java.net.URI;

public class IndexResource implements Resource {

    private final URI uri;
    private final DirectoryExplorer directoryExplorer;

    public IndexResource(URI uri, DirectoryExplorer directoryExplorer) {
        this.uri = uri;
        this.directoryExplorer = directoryExplorer;
    }

    @Override
    public byte[] generateResponse(Request request) {
        String response = Response.STATUS_TWO_HUNDRED + "\n" + directoryExplorer.generateHTMLListing();
        return response.getBytes();
    }

    @Override
    public URI uri() {
        return uri;
    }
}
