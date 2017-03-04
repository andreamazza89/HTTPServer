package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.middleware.*;
import com.andreamazzarella.http_server.middleware.controllers.MethodOptionsController;
import com.andreamazzarella.http_server.middleware.controllers.StaticAssetsController;
import com.andreamazzarella.http_server.middleware.controllers.TeaPotController;
import com.andreamazzarella.http_server.request.Request;

import java.io.IOException;
import java.net.URI;
import java.util.*;

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
        MiddleWare teapotRoute = new TeaPotController();
MiddleWare bogusIndex = new Bogus();
        routes.put(URI.create("/"), bogusIndex);
        routes.put(URI.create("/coffee"), teapotRoute);
        routes.put(URI.create("/method_options"), new MethodOptionsController());

        FileSystem loggingFileSystem = new FileSystem(URI.create(loggingDirectory));
        FileSystem staticFileSystem = new FileSystem(URI.create(publicDirectory));

        List<URI> routesToLog = new ArrayList<>(Arrays.asList(URI.create("log"), URI.create("these"), URI.create("requests")));

        MiddleWare staticResourcesController = new StaticAssetsController(staticFileSystem);
        MiddleWare router = new Router(routes, staticResourcesController);
        List<User> users = new ArrayList<>(Arrays.asList(new User("admin", "hunter2")));
        List<URI> routesToAuthenticate = new ArrayList<>(Arrays.asList(URI.create("/logs")));
        MiddleWare authenticator = new BasicAuthenticator(router, users, routesToAuthenticate);
        MiddleWare logger = new Logger(authenticator, loggingFileSystem, URI.create("/logs"), routesToLog);

        return logger;
    }

    private static class Bogus implements MiddleWare{
        @Override
        public Response generateResponseFor(Request request) {
            return new Response(_404);
        }
    }
}
