package com.andreamazzarella.http_server;

import java.net.URI;

class MissingResource extends Resource {

    MissingResource(URI resource) {
        super(resource);
    }

    @Override
    public Request.Method[] methodsAllowed() {
        return new Request.Method[] {Request.Method.PUT};
    }

}
