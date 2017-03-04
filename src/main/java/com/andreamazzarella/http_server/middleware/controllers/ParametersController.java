package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

import java.util.Map;

public class ParametersController extends BaseController {

    protected Response get(Request request) {
        Map<String, String> allParameters = request.getParams();
        String body = "";

        for (String parameterKey : allParameters.keySet()) {
            body += String.format("%s = %s", parameterKey, allParameters.get(parameterKey));
        }

        return new Response(Response.StatusCode._200).setBody(body.getBytes());
    }
}
