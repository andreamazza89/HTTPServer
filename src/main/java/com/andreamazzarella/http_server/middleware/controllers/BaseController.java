package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Header;
import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request.Request;

import java.lang.reflect.Method;

import static com.andreamazzarella.http_server.Response.StatusCode._200;

abstract class BaseController implements MiddleWare{

    @Override
    public Response generateResponseFor(Request request) {
        switch (request.getMethod()) {
            case OPTIONS:
                Method[] methods = this.getClass().getDeclaredMethods();
                String methodsAllowed = "";
                for (Method method : methods) {
                    if (!method.isBridge()) {
                        methodsAllowed += method.getName().toUpperCase() + ",";
                    }
                }
                methodsAllowed += "OPTIONS";

                Header optionsHeader = new Header("Allow", methodsAllowed);
                Response response = new Response(_200).addHeader(optionsHeader);
                return response;
            default:
                return new Response(_200);
        }

    }
}
