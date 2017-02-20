package com.andreamazzarella.http_server;

class Router {

    private final DataExchange socketConnection;
    private final Routes routes;

    Router(DataExchange socketConnection, Routes routes) {
        this.socketConnection = socketConnection;
        this.routes = routes;
    }

    void respondToClient() {
        Request request = new Request(socketConnection);
        String response = routes.generateResponse(request);
        socketConnection.write(response);
///////////////////////////////////////
        System.out.println(request.uri());
//////////////////////////////////////
    }
}
