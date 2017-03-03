package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.MWResponse;
import com.andreamazzarella.http_server.request.Request;

import java.net.URI;
import java.util.Map;

import static com.andreamazzarella.http_server.MWResponse.StatusCode._404;

public class MWRouter implements MiddleWare {

    private final Map<URI, MiddleWare> routes;
    private final MiddleWare staticResourcesController;
    private final FileSystem staticFilesystem;

    MWRouter(Map<URI, MiddleWare> routes, MiddleWare staticResourcesController, FileSystem staticFilesystem) {
        this.routes = routes;
        this.staticResourcesController = staticResourcesController;
        this.staticFilesystem = staticFilesystem;
    }

    @Override
    public MWResponse generateResponseFor(Request request) {
        if (controllerForRouteExists(request)) {
            MiddleWare controller = routes.get(request.getUri());
            return controller.generateResponseFor(request);
        }

        if (staticFilesystem.doesResourceExist(request.getUri())) {
            return staticResourcesController.generateResponseFor(request);
        } else {
            return new MWResponse(_404);
        }
    }

    private boolean controllerForRouteExists(Request request) {
        return routes.keySet().contains(request.getUri());
    }
}
