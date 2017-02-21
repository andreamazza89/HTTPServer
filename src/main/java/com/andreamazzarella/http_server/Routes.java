package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Routes {

    private List<Route> routes = new ArrayList<>();

    void addRoute(Route route) {
        routes.add(route);
    }

    Route findRoute(URI uri) {
        Optional<Route> routeFound = routes.stream().filter((route) -> route.uri().equals(uri)).findFirst();
        return routeFound.orElse(new MissingRoute(uri));
    }
}
