package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.Routes;
import com.andreamazzarella.http_server.Request;

public class StubbedRoutes extends Routes {

    @Override
    public String generateResponse(Request request) {
        return "stubbed response";
    }

}
