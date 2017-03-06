package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Request;

import java.net.URI;
import java.util.Map;

public class Router implements MiddleWare {

    private final Map<URI, MiddleWare> routes;
    private final MiddleWare staticResourcesController;

    public Router(Map<URI, MiddleWare> routes, MiddleWare staticResourcesController) {
        this.routes = routes;
        this.staticResourcesController = staticResourcesController;
    }

    @Override
    public Response generateResponseFor(Request request) {
        if (controllerForRouteExists(request)) {
            MiddleWare controller = routes.get(request.getUri());
            return controller.generateResponseFor(request);
        } else {
            return staticResourcesController.generateResponseFor(request);
        }
    }

    private boolean controllerForRouteExists(Request request) {
        return routes.keySet().contains(request.getUri());
    }
}
