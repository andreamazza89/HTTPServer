package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.*;

public class Routes {

    private List<Route> routes = new ArrayList<>();

    void addRoute(Route route) {
        routes.add(route);
    }

    public String generateResponse(Request request) {
        URI resource = URI.create(request.extractRequestURI());
        Optional<Route> route = findRoute(resource);
        String response = "";

        if (!route.isPresent()) {
            response = "HTTP/1.1 404 Not Found\n\n";
        } else if (request.method() == Request.Method.OPTIONS) {
            Request.Method[] methods = route.get().methodsAllowed();
            String methodsAllowed = "";
            for (Request.Method method : methods) {
                methodsAllowed += method.toString() + ",";
            }
            methodsAllowed = methodsAllowed.substring(0, methodsAllowed.length()-1);
            response = "HTTP/1.1 200 OK\nAllow: " + methodsAllowed + "\n\n";
        } else if (request.method() == Request.Method.GET) {
            if (route.get().isRedirectRoute()) {
                response = "HTTP/1.1 302 Found\nLocation: "+route.get().redirectLocation()+"\n\n";
            } else {
                response = "HTTP/1.1 200 OK\n\n";
            }
        }
        return response;
    }

    private Optional<Route> findRoute(URI resource) {
        return routes.stream().filter((route) -> route.getResource().equals(resource)).findFirst();
    }

}
