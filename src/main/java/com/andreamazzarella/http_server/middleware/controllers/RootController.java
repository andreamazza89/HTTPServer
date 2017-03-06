package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.utilities.DirectoryExplorer;
import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Request;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode._200;

public class RootController extends BaseController {

    private final DirectoryExplorer directoryExplorer;

    public RootController(DirectoryExplorer directoryExplorer) {
        this.directoryExplorer = directoryExplorer;
    }

    protected Response get(Request request) {
        byte[] directoryListing = directoryExplorer.generateHTMLListing().getBytes();
        return new Response(_200).setBody(directoryListing);
    }

}
