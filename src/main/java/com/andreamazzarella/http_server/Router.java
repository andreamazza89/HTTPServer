package com.andreamazzarella.http_server;

class Router {

    private final DataExchange socketConnection;
    private final Routes routes;

    Router(DataExchange socketConnection, Routes routes) {
        this.socketConnection = socketConnection;
        this.routes = routes;
    }

    void respondToRequest() {
        //I feel reading the incoming request message should happen here and then the Request is just given the raw message
        Request request = new Request(socketConnection);
        String response = "no response";

///////////////////////////////////////
System.out.println(request.toString());
//////////////////////////////////////

        String route = request.extractRequestURI();

        if (!routes.doesRouteExist(route)) {
            response = "HTTP/1.1 404 Not Found\n\n";
        } else if (request.method() == Request.Method.OPTIONS) {
            Request.Method[] methods = routes.methodsAllowed(route);
            String methodsAllowed = "";
            for (Request.Method method : methods) {
                methodsAllowed += method.toString() + ",";
            }
            methodsAllowed = methodsAllowed.substring(0, methodsAllowed.length()-1);
            response = "HTTP/1.1 200 OK\nAllow: " + methodsAllowed + "\n\n";
        } else if (request.method() == Request.Method.GET) {
            if (routes.isRedirectRoute(route)) {
                response = "HTTP/1.1 302 Found\nLocation: "+routes.redirectLocation(route)+"\n\n";
            } else {
                response = "HTTP/1.1 200 OK\n\n";
            }
        }

        socketConnection.write(response);
    }
}
