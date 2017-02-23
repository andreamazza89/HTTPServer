package com.andreamazzarella.http_server;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws IOException {
        InputParser inputParser = new InputParser(args);
        int portNumber = inputParser.parsePortNumber();
        String publicDirectory = inputParser.parseDirectoryPath();

        HTTPServer server = new HTTPServer(portNumber, publicDirectory, generateResources());
        server.start();
    }

    private static Resources generateResources() {
        Resources resources = new Resources();
        URI resourcesBasePath = URI.create("./resources/");
        Blaah fileSystem = new Blaah(resourcesBasePath);

        Resource root = new Resource(URI.create("/"), fileSystem);
        Resource methodOptions = new Resource(URI.create("/method_options"), fileSystem);
        Resource methodOptionsTwo = new Resource(URI.create("/method_options2"), fileSystem);
        Resource form = new Resource(URI.create("/form"), fileSystem);
        Resource redirect = new Resource(URI.create("/redirect"), fileSystem);
        Resource coffee = new Resource(URI.create("/coffee"), fileSystem);
        Resource tea = new Resource(URI.create("/tea"), fileSystem);

        root.allowMethods(new Request.Method[] {Request.Method.GET});
        methodOptions.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.HEAD,
                Request.Method.POST, Request.Method.OPTIONS, Request.Method.PUT});
        methodOptionsTwo.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});
        form.allowMethods(new Request.Method[] {Request.Method.POST, Request.Method.PUT});
        redirect.allowMethods(new Request.Method[] {Request.Method.GET});
        tea.allowMethods(new Request.Method[] {Request.Method.GET});

        redirect.setRedirect(URI.create("http://localhost:5000/"));
        coffee.setTeaPot();

        resources.addRoute(root);
        resources.addRoute(methodOptions);
        resources.addRoute(methodOptionsTwo);
        resources.addRoute(form);
        resources.addRoute(redirect);
        resources.addRoute(tea);
        resources.addRoute(coffee);

        return resources;
    }
}
