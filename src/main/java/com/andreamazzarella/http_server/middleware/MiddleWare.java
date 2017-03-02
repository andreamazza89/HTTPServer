package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.MWResponse;
import com.andreamazzarella.http_server.request.Request;

public interface MiddleWare {
    MWResponse generateResponseFor(Request request);
}
