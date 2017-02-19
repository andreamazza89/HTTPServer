package com.andreamazzarella.http_server;

import java.util.Hashtable;
import java.util.Map;

public class Routes {

    private Map<String, Request.Method[]> routes = new Hashtable<>();

    public void addRoute(String route, Request.Method[] methods) {
        routes.put(route, methods);
    }

    public boolean isRouteAvailable(String route) {
        return routes.containsKey(route);
    }

    public Request.Method[] methodsAllowed(String route) {
        return routes.get(route);
    }
}
