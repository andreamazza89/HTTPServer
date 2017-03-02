package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.MWResponse;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request.Request;

public class FakeMiddleWare implements MiddleWare {

    private MWResponse stubbedResponse;

    public void stubResponse(MWResponse stubbedResponse) {
        this.stubbedResponse = stubbedResponse;
    }

    @Override
    public MWResponse generateResponseFor(Request request) {
        return stubbedResponse;
    }
}
