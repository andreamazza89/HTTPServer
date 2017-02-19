package com.andreamazzarella.http_server;

class Router {

    private final DataExchange socketConnection;

    Router(DataExchange socketConnection) {
        this.socketConnection = socketConnection;
    }

    void respondToRequest() {
        Request request = new Request(socketConnection);

///////////////////////////////////////
System.out.println(request.toString());
//////////////////////////////////////

        String uri = request.extractRequestURI();

        // if resource is not in the resources available (configuration somehow imported), return a 404;
        // otherwise, return the appropriate response;



        if (uri.equals("/foobar")) {
            socketConnection.write("HTTP/1.1 404 Not Found\n");
        } else if(uri.equals("/method_options")) {
            socketConnection.write("HTTP/1.1 200 OK\nAllow: GET,HEAD,POST,OPTIONS,PUT\n");
        } else if(uri.equals("/method_options2")) {
            socketConnection.write("HTTP/1.1 200 OK\nAllow: GET,OPTIONS\n");
        } else {
            socketConnection.write("HTTP/1.1 200 OK\n");
        }
    }
}
