package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Optional;

public interface Resource {
    byte[] generateResponse(Request request);
    Optional<URI> uri();
}
