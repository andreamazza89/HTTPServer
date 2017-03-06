package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request_response.Request;

public class FakeMiddleWare implements MiddleWare {

    private Response stubbedResponse;

    public void stubResponse(Response stubbedResponse) {
        this.stubbedResponse = stubbedResponse;
    }

    @Override
    public Response generateResponseFor(Request request) {
        return stubbedResponse;
    }
}
