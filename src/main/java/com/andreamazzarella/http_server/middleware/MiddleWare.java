package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Request;

public interface MiddleWare {
    Response generateResponseFor(Request request);
}
