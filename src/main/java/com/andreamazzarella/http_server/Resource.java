package com.andreamazzarella.http_server;

import java.net.URI;

public interface Resource {
    byte[] generateResponse(Request request);
    URI uri();
}
