package com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;

import java.net.URI;

public interface Resource {
    byte[] generateResponse(Request request);
    URI uri();
}