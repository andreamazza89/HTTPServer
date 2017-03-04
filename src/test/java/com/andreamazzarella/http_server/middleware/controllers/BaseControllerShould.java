package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request.Request;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Optional;

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
        assertEquals("GET,POST,OPTIONS", response.getHeaders().get(0).getValue());
    }


    private class TestControllerOne extends BaseController {}

    private class TestControllerTwo extends BaseController {
        protected Response get(Request request) {
            throw new NotImplementedException();
        }

        protected Response post(Request request) {
            throw new NotImplementedException();
        }
    }
}
