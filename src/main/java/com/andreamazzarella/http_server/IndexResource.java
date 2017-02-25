package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

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
    public Optional<URI> uri() {
        return Optional.of(uri);
    }
}
