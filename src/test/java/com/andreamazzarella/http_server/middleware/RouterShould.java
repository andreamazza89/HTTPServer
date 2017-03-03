package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeMiddleWare;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.andreamazzarella.http_server.Response.StatusCode._200;
import static com.andreamazzarella.http_server.Response.StatusCode._404;
import static com.andreamazzarella.http_server.Response.StatusCode._418;
import static org.junit.Assert.assertEquals;

public class RouterShould {

    @Test
    public void delegateResponseGenerationToAControllerIfOneExistsForTheRequestPathExampleOne() {
        FakeMiddleWare controller = new FakeMiddleWare();
        controller.stubResponse(new Response(_200));
        Map<URI, MiddleWare> routes = new HashMap<>();
        URI test_path = URI.create("/test_route");
        routes.put(test_path, controller);
        Request request = new Request("GET /test_route HTTP/1.1", new ArrayList<>(), Optional.empty());
        MiddleWare router = new Router(routes, new FakeMiddleWare(), new FakeFileSystem(URI.create("./path_to_public_directory")));

        Response response = router.generateResponseFor(request);

        assertEquals(_200, response.getStatusCode());
    }

    @Test
    public void delegateResponseGenerationToAControllerIfOneExistsForTheRequestPathExampleTwo() {
        FakeMiddleWare controller = new FakeMiddleWare();
        controller.stubResponse(new Response(_418));
        Map<URI, MiddleWare> routes = new HashMap<>();
        URI test_path = URI.create("/test_route");
        routes.put(test_path, controller);
        Request request = new Request("GET /test_route HTTP/1.1", new ArrayList<>(), Optional.empty());
        MiddleWare router = new Router(routes, new FakeMiddleWare(), new FakeFileSystem(URI.create("./path_to_public_directory")));

        Response response = router.generateResponseFor(request);

        assertEquals(_418, response.getStatusCode());
    }

    @Test
    public void lookForTheResourceInTheFilesystemIfThereIsNoControllerForTheRequestPath() {
        FakeFileSystem filesystem = new FakeFileSystem(URI.create("./path_to_public_directory"));
        filesystem.setResourceExistsFlagTo(true);
        Request request = new Request("GET /path_to_static_resource HTTP/1.1", new ArrayList<>(), Optional.empty());
        FakeMiddleWare staticResourcesController = new FakeMiddleWare();
        staticResourcesController.stubResponse(new Response(_200));
        MiddleWare router = new Router(new HashMap<>(), staticResourcesController,filesystem);

        Response response = router.generateResponseFor(request);

        assertEquals(_200, response.getStatusCode());
    }

    @Test
    public void respondsWith404IfAllElseFails() {
        Request request = new Request("GET /lol/not/really/real/am/i HTTP/1.1", new ArrayList<>(), Optional.empty());
        MiddleWare router = new Router(new HashMap<>(), new FakeMiddleWare(), new FakeFileSystem(URI.create("./path_to_public_directory")));

        Response response = router.generateResponseFor(request);

        assertEquals(_404, response.getStatusCode());
    }
}
