package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.utilities.DirectoryExplorer;

import java.net.URI;

public class FakeDirectoryExplorer extends DirectoryExplorer {

    private String listing;

    public FakeDirectoryExplorer(URI directoryPath) {
        super(directoryPath);
    }

    public void setHTMLListingTo(String listing) {
        this.listing = listing;
    }

    @Override
    public String generateHTMLListing() {
        return listing;
    }
}
