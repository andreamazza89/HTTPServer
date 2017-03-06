package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request_response.Request;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode._405;
import static com.andreamazzarella.http_server.request_response.Response.StatusCode._418;
import static org.junit.Assert.assertEquals;

public class BaseControllerShould {

    @Test
    public void generateAnOptionsResponseBasedOnTheMethodsAcceptedExampleOne() {
        Request request = new Request("OPTIONS /path HTTP/1.1", new ArrayList<>(), Optional.empty());
        MiddleWare testController = new TestControllerOne();

        Response response = testController.generateResponseFor(request);

        assertEquals("Allow", response.getHeaders().get(0).getName());
        assertEquals("OPTIONS", response.getHeaders().get(0).getValue());
    }

    @Test
    public void generateAnOptionsResponseBasedOnTheMethodsAcceptedExampleTwo() {
        Request request = new Request("OPTIONS /path HTTP/1.1", new ArrayList<>(), Optional.empty());
        MiddleWare testController = new TestControllerTwo();

        Response response = testController.generateResponseFor(request);

        assertEquals("Allow", response.getHeaders().get(0).getName());
        assertEquals("GET,OPTIONS", response.getHeaders().get(0).getValue());
    }

    @Test
    public void generateA405NotAllowedIfTheMethodIsNotAllowed() {
        Request request = new Request("GET /path HTTP/1.1", new ArrayList<>(), Optional.empty());
        MiddleWare testController = new TestControllerOne();

        Response response = testController.generateResponseFor(request);

        assertEquals(_405, response.getStatusCode());
    }

    @Test
    public void delegateToTheExtendedControllerIfTheMethodIsAllowed() {
        Request request = new Request("GET /path HTTP/1.1", new ArrayList<>(), Optional.empty());
        MiddleWare testController = new TestControllerTwo();

        Response response = testController.generateResponseFor(request);

        assertEquals(_418, response.getStatusCode());
        assertEquals("yet another response body", new String(response.getBody().get()));
    }

    private class TestControllerOne extends BaseController {}

    private class TestControllerTwo extends BaseController {
        @Override
        protected Response get(Request request) {
            return new Response(_418).setBody("yet another response body".getBytes());
        }
    }
}
