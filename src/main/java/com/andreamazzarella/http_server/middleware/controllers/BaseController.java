package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Header;
import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request.Request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static com.andreamazzarella.http_server.Response.StatusCode._200;
import static com.andreamazzarella.http_server.Response.StatusCode._405;
import static com.andreamazzarella.http_server.request.Request.Method.OPTIONS;

abstract class BaseController implements MiddleWare{

    @Override
    public Response generateResponseFor(Request request) {
        Method[] methodsAllowed = this.getClass().getDeclaredMethods();
        String downCaseRequestMethod = request.getMethod().toString().toLowerCase();

        if (request.getMethod() == OPTIONS) {
            Header optionsHeader = generateOptionsHeader(methodsAllowed);
            return new Response(_200).addHeader(optionsHeader);
        } else {
            Optional<Method> methodFound = searchMethod(methodsAllowed, downCaseRequestMethod);
            return methodFound.isPresent() ? invokeMethod(request, methodFound) : new Response(_405);
        }

    }

    private Header generateOptionsHeader(Method[] methodsAllowed) {
        String methodsAllowedHeaderValue = generateMethodsAllowedHeaderValue(methodsAllowed);
        return new Header("Allow", methodsAllowedHeaderValue);
    }

    private String generateMethodsAllowedHeaderValue(Method[] methodsAllowed) {
        String methodsAllowedHeaderValue = "";
        for (Method method : methodsAllowed) {
            if (!method.isBridge()) {
                methodsAllowedHeaderValue += method.getName().toUpperCase() + ",";
            }
        }
        return methodsAllowedHeaderValue + "OPTIONS";
    }

    private Optional<Method> searchMethod(Method[] methodsAllowed, String method) {
        return Arrays.stream(methodsAllowed)
                .filter((methodAllowed) -> methodAllowed.getName().equals(method))
                .findFirst();
    }

    private Response invokeMethod(Request request, Optional<Method> methodFound) {
        try {
            return (Response)methodFound.get().invoke(this, request);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("something went wrong invoking the controller's method");
        }
    }
}
