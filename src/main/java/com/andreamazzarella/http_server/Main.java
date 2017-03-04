package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.middleware.*;
import com.andreamazzarella.http_server.middleware.controllers.*;
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
        URI logsFileName = URI.create("/logs_are_here");
        FileSystem loggingFileSystem = new FileSystem(URI.create(loggingDirectory));
        FileSystem staticFileSystem = new FileSystem(URI.create(publicDirectory));

        Map<URI, MiddleWare> routes = new HashMap<>();
        routes.put(URI.create("/"), new Bogus());
        routes.put(URI.create("/logs"), new LogsController(loggingFileSystem, logsFileName));
        routes.put(URI.create("/coffee"), new TeaPotController());
        routes.put(URI.create("/tea"), new SimpleController());
        routes.put(URI.create("/method_options"), new MethodOptionsController());
        routes.put(URI.create("/method_options2"), new MethodOptionsTwoController());

        List<URI> routesToLog = new ArrayList<>(Arrays.asList(URI.create("/log"), URI.create("/these"), URI.create("/requests")));

        MiddleWare staticResourcesController = new StaticAssetsController(staticFileSystem);
        MiddleWare router = new Router(routes, staticResourcesController);
        List<User> users = new ArrayList<>(Arrays.asList(new User("admin", "hunter2")));
        List<URI> routesToAuthenticate = new ArrayList<>(Arrays.asList(URI.create("/logs")));
        MiddleWare authenticator = new BasicAuthenticator(router, users, routesToAuthenticate);
        MiddleWare logger = new Logger(authenticator, loggingFileSystem, logsFileName, routesToLog);

        return logger;
    }

    private static class Bogus implements MiddleWare{
        @Override
        public Response generateResponseFor(Request request) {
            return new Response(_404);
        }
    }
}
