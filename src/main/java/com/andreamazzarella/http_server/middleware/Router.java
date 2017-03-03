package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

import java.net.URI;
import java.util.Map;

import static com.andreamazzarella.http_server.Response.StatusCode._404;

public class Router implements MiddleWare {

    private final Map<URI, MiddleWare> routes;
    private final MiddleWare staticResourcesController;
    private final FileSystem staticFilesystem;

    public Router(Map<URI, MiddleWare> routes, MiddleWare staticResourcesController, FileSystem staticFilesystem) {
        this.routes = routes;
        this.staticResourcesController = staticResourcesController;
        this.staticFilesystem = staticFilesystem;
    }

    @Override
    public Response generateResponseFor(Request request) {
        if (controllerForRouteExists(request)) {
            MiddleWare controller = routes.get(request.getUri());
            return controller.generateResponseFor(request);
        }

        if (staticFilesystem.doesResourceExist(request.getUri())) {
            return staticResourcesController.generateResponseFor(request);
        } else {
            return new Response(_404);
        }
    }

    private boolean controllerForRouteExists(Request request) {
        return routes.keySet().contains(request.getUri());
    }
}
