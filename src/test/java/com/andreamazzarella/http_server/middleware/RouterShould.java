package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Request;
import com.andreamazzarella.http_server.support.FakeMiddleWare;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode._200;
import static com.andreamazzarella.http_server.request_response.Response.StatusCode._418;
import static org.junit.Assert.assertEquals;

public class RouterShould {

    @Test
    public void delegateResponseGenerationToAControllerIfOneExistsForTheRequestPathExampleOne() {
        FakeMiddleWare controller = new FakeMiddleWare();
        Response expectedResponse = new Response(_200);
        controller.stubResponse(expectedResponse);
        Map<URI, MiddleWare> routes = new HashMap<>();
        URI test_path = URI.create("/test_route");
        routes.put(test_path, controller);
        Request request = new Request("GET /test_route HTTP/1.1", new ArrayList<>(), Optional.empty());
        MiddleWare router = new Router(routes, new FakeMiddleWare());

        Response response = router.generateResponseFor(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void delegateResponseGenerationToAControllerIfOneExistsForTheRequestPathExampleTwo() {
        FakeMiddleWare controller = new FakeMiddleWare();
        Response expectedResponse = new Response(_418);
        controller.stubResponse(expectedResponse);
        Map<URI, MiddleWare> routes = new HashMap<>();
        URI test_path = URI.create("/test_route");
        routes.put(test_path, controller);
        Request request = new Request("GET /test_route HTTP/1.1", new ArrayList<>(), Optional.empty());
        MiddleWare router = new Router(routes, new FakeMiddleWare());

        Response response = router.generateResponseFor(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void delegateResponseGenerationToTheStaticResourcesControllerIfThereIsNoOtherControllerForTheRequestPath() {
        Request request = new Request("GET /path_to_static_resource HTTP/1.1", new ArrayList<>(), Optional.empty());
        FakeMiddleWare staticResourcesController = new FakeMiddleWare();
        Response expectedResponse = new Response(_200);
        staticResourcesController.stubResponse(expectedResponse);
        MiddleWare router = new Router(new HashMap<>(), staticResourcesController);

        Response response = router.generateResponseFor(request);

        assertEquals(expectedResponse, response);
    }
}
