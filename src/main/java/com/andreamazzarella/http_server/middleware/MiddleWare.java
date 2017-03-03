package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

public interface MiddleWare {
    Response generateResponseFor(Request request);
}
