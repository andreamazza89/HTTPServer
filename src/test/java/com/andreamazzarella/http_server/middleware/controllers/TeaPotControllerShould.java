package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.request_response.Header;
import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request_response.Request;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode._418;
import static org.junit.Assert.assertEquals;

public class TeaPotControllerShould {

    @Test
    public void generateATeapotResponse() {
        Request request = new Request("GET /teapot HTTP/1.1", new ArrayList<Header>(), Optional.empty());
        MiddleWare teaPotController = new TeaPotController();

        Response response = teaPotController.generateResponseFor(request);

        assertEquals(_418, response.getStatusCode());
        assertEquals("I'm a teapot", new String(response.getBody().get()));
    }
}
