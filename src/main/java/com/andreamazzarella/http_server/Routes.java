package com.andreamazzarella.http_server;

import java.util.Hashtable;
import java.util.Map;

class Routes {

    private Map<String, Request.Method[]> routes = new Hashtable<>();
    private Map<String, String> redirectable = new Hashtable<>();

    void addRoute(String route, Request.Method[] methods) {
        routes.put(route, methods);
    }

    boolean doesRouteExist(String route) {
        return routes.containsKey(route);
    }



    /////////////this is specific to an individual route

    Request.Method[] methodsAllowed(String route) {
        return routes.get(route);
    }

    public void setRedirect(String route, String location) {
        redirectable.put(route, location);
    }

    public boolean isRedirectRoute(String route) {
        return redirectable.containsKey(route);
    }

    public String redirectLocation(String route) {
        return redirectable.get(route);
    }

}
