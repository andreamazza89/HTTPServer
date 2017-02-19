package com.andreamazzarella.http_server;

import java.util.Hashtable;
import java.util.Map;

class Router {

    private final DataExchange socketConnection;

    Router(DataExchange socketConnection) {
        this.socketConnection = socketConnection;
    }

    void respondToRequest() {
        //I feel reading the incoming request message should happen here and then the Request is just given the raw message
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
            response = "HTTP/1.1 404 Not Found\n\n";
        }


        if (uri.equals("/method_options")) {
            socketConnection.write("HTTP/1.1 200 OK\nAllow: GET,HEAD,POST,OPTIONS,PUT\n\n");

        } else if (uri.equals("/method_options2")) {
            socketConnection.write("HTTP/1.1 200 OK\nAllow: GET,OPTIONS\n\n");

        } else if (uri.equals("/") || uri.equals("/form")) {
            socketConnection.write("HTTP/1.1 200 OK\n\n");
        }

        socketConnection.write(response);

        // this always happens:       socketConnection.write(response.toString);
    }
}
