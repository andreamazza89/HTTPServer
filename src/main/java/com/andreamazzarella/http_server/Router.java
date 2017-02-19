package com.andreamazzarella.http_server;

import java.util.Hashtable;
import java.util.Map;

class Router {

    private final DataExchange socketConnection;

    Router(DataExchange socketConnection) {
        this.socketConnection = socketConnection;
    }

    void respondToRequest() {
        Request request = new Request(socketConnection);
        String response = "";

///////////////////////////////////////
System.out.println(request.toString());
//////////////////////////////////////

        String uri = request.extractRequestURI();

        Map<String, String[]> routes = new Hashtable<>();
        routes.put("/", new String[]{"GET"});
        routes.put("/method_options", new String[]{"GET", "HEAD", "POST", "OPTIONS", "PUT"});
        routes.put("/method_options2", new String[]{"GET", "OPTIONS"});

        if (!routes.containsKey(uri)) {
            response = "HTTP/1.1 404 Not Found\n";
        }


        if (uri.equals("/method_options")) {
            socketConnection.write("HTTP/1.1 200 OK\nAllow: GET,HEAD,POST,OPTIONS,PUT\n");

        } else if (uri.equals("/method_options2")) {
            socketConnection.write("HTTP/1.1 200 OK\nAllow: GET,OPTIONS\n");

        } else if (uri.equals("/") || uri.equals("/form")) {
            socketConnection.write("HTTP/1.1 200 OK\n");

        }

        socketConnection.write(response);
    }
}
