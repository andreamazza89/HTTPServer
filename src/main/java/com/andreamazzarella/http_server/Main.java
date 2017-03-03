package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.middleware.*;
import com.andreamazzarella.http_server.middleware.controllers.StaticAssetsController;
import com.andreamazzarella.http_server.request.Request;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.andreamazzarella.http_server.Response.StatusCode._404;

public class Main {
    public static void main(String[] args) throws IOException {
        InputParser inputParser = new InputParser(args);
        int portNumber = inputParser.parsePortNumber();
        String publicDirectory = inputParser.parseDirectoryPath();
        String loggingDirectory = "./logs/";

        MiddleWare middleWareStack = createMiddleWareStack(publicDirectory, loggingDirectory);

        HTTPServer server = new HTTPServer(portNumber, middleWareStack);
        System.out.println("Server running on port: " + portNumber);
        server.start();
    }

    private static MiddleWare createMiddleWareStack(String publicDirectory, String loggingDirectory) {
        Map<URI, MiddleWare> routes = new HashMap<>();
        MiddleWare bogusIndex = new Bogus();
        routes.put(URI.create("/"), bogusIndex);

        FileSystem loggingFileSystem = new FileSystem(URI.create(loggingDirectory));
        FileSystem staticFileSystem = new FileSystem(URI.create(publicDirectory));

        MiddleWare staticResourcesController = new StaticAssetsController();
        MiddleWare router = new Router(routes, staticResourcesController, staticFileSystem);
        MiddleWare authenticator = new BasicAuthenticator(router);
        MiddleWare logger = new Logger(authenticator, loggingFileSystem);

        return logger;
    }

    private static class Bogus implements MiddleWare{
        @Override
        public Response generateResponseFor(Request request) {
            return new Response(_404);
        }
    }


}
