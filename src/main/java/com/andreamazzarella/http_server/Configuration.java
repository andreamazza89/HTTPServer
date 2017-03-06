package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.middleware.*;
import com.andreamazzarella.http_server.middleware.controllers.*;
import com.andreamazzarella.http_server.utilities.DirectoryExplorer;
import com.andreamazzarella.http_server.utilities.FileSystem;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Configuration {

    static MiddleWare createMiddleWareStack(String publicDirectory, String loggingDirectory) {
        String logsFileName = "/logs_go_here";

        MiddleWare router = assembleRouter(publicDirectory, loggingDirectory, logsFileName);
        MiddleWare authenticator = assembleAuthenticator(router);
        MiddleWare logger = assembleLogger(authenticator, loggingDirectory, logsFileName);

        return logger;
    }

    private static MiddleWare assembleRouter(String publicDirectory, String loggingDirectory, String logsFileName) {
        DirectoryExplorer directoryExplorer = new DirectoryExplorer(URI.create(publicDirectory));
        FileSystem loggingFileSystem = new FileSystem(URI.create(loggingDirectory));
        FileSystem staticFileSystem = new FileSystem(URI.create(publicDirectory));
        URI logsFilePath = URI.create(logsFileName);

        Map<URI, MiddleWare> routes = new HashMap<>();
        routes.put(URI.create("/"), new RootController(directoryExplorer));
        routes.put(URI.create("/logs"), new LogsController(loggingFileSystem, logsFilePath));
        routes.put(URI.create("/coffee"), new TeaPotController());
        routes.put(URI.create("/tea"), new SimpleController());
        routes.put(URI.create("/method_options"), new MethodOptionsController());
        routes.put(URI.create("/method_options2"), new MethodOptionsTwoController());
        routes.put(URI.create("/parameters"), new ParametersController());
        routes.put(URI.create("/form"), new FormController());
        routes.put(URI.create("/redirect"), new RedirectController());
        routes.put(URI.create("/cookie"), new CookieController());
        routes.put(URI.create("/eat_cookie"), new EatCookieController());

        MiddleWare staticResourcesController = new StaticAssetsController(staticFileSystem);

        return new Router(routes, staticResourcesController);
    }

    private static MiddleWare assembleAuthenticator(MiddleWare nextLayer) {
        List<User> users = new ArrayList<>(Arrays.asList(new User("admin", "hunter2")));
        List<URI> routesToAuthenticate = new ArrayList<>(Arrays.asList(URI.create("/logs")));
        return new BasicAuthenticator(nextLayer, users, routesToAuthenticate);
    }


    private static MiddleWare assembleLogger(MiddleWare authenticator, String loggingDirectory, String logsFileName) {
        FileSystem loggingFileSystem = new FileSystem(URI.create(loggingDirectory));
        List<URI> routesToLog = new ArrayList<>(Arrays.asList(URI.create("/log"), URI.create("/these"), URI.create("/requests")));
        return new Logger(authenticator, loggingFileSystem, URI.create(logsFileName), routesToLog);
    }


}
