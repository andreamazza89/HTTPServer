package com.andreamazzarella.http_server.resources;
import com.andreamazzarella.http_server.request.Request;

import java.net.URI;

public interface Resource {
    byte[] generateResponse(Request request);
    URI uri();
}
