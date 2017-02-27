package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.resources.*;

import java.io.IOException;
import java.net.URI;

public class Main {
    public static void main(String[] args) throws IOException {
        InputParser inputParser = new InputParser(args);
        int portNumber = inputParser.parsePortNumber();
        String publicDirectory = inputParser.parseDirectoryPath();
        FileSystem logsFileSystem = new FileSystem(URI.create("./logs"));
        Logger logger = new Logger(logsFileSystem);
        logger.follow(URI.create("/log"));
        logger.follow(URI.create("/these"));
        logger.follow(URI.create("/requests"));


        HTTPServer server = new HTTPServer(portNumber, publicDirectory, generateResources(), logger);
        server.start();
    }

    private static Resources generateResources() {
        URI publicPath = URI.create("/Users/Andrea/Dropbox/Programming/repos/java/cob_spec/public/");
        URI dynamicPath = URI.create("./resources");
        URI logsPath = URI.create("./logs");

        BasicAuthenticator authenticator = new BasicAuthenticator();
        authenticator.addUser("admin", "hunter2");

        Resources resources = new Resources(authenticator);

        FileSystem staticFilesystem = new FileSystem(publicPath);
        FileSystem dynamicFilesystem = new FileSystem(dynamicPath);
        FileSystem logsFilesystem = new FileSystem(logsPath);
        DirectoryExplorer directoryExplorer = new DirectoryExplorer(publicPath);

        dynamicFilesystem.addOrReplaceResource(URI.create("/cookie"), "Eat".getBytes());
        dynamicFilesystem.addOrReplaceResource(URI.create("/eat_cookie"), "mmmm ".getBytes());

        Resource root = new IndexResource(URI.create("/"), directoryExplorer);
        Resource form = new DynamicResource(URI.create("/form"), dynamicFilesystem, new Request.Method[]
                {Request.Method.GET, Request.Method.POST, Request.Method.PUT, Request.Method.DELETE});
        Resource methodOptions = new DynamicResource(URI.create("/method_options"), dynamicFilesystem, new Request.Method[]
                {Request.Method.GET, Request.Method.HEAD, Request.Method.POST, Request.Method.OPTIONS, Request.Method.PUT});
        Resource methodOptionsTwo = new DynamicResource(URI.create("/method_options2"), dynamicFilesystem, new Request.Method[]
                {Request.Method.GET, Request.Method.OPTIONS});
        Resource tea = new DynamicResource(URI.create("/tea"), dynamicFilesystem, new Request.Method[] {Request.Method.GET});
        Resource parameters = new DynamicResource(URI.create("/parameters"), dynamicFilesystem, new Request.Method[] {Request.Method.GET});
        Resource log = new DynamicResource(URI.create("/log"), dynamicFilesystem, new Request.Method[] {Request.Method.GET});
        Resource these = new DynamicResource(URI.create("/these"), dynamicFilesystem, new Request.Method[] {Request.Method.GET});
        Resource requests = new DynamicResource(URI.create("/requests"), dynamicFilesystem, new Request.Method[] {Request.Method.GET});
        Resource logs = new DynamicResource(URI.create("/logs"), logsFilesystem, new Request.Method[] {Request.Method.GET});
        Resource coffee = new TeaPotResource(URI.create("/coffee"));
        Resource fileOne = new DynamicResource(URI.create("/file1"), staticFilesystem, new Request.Method[] {Request.Method.GET});
        Resource imageJPEG = new DynamicResource(URI.create("/image.jpeg"), staticFilesystem, new Request.Method[] {Request.Method.GET});
        Resource imagePNG = new DynamicResource(URI.create("/image.png"), staticFilesystem, new Request.Method[] {Request.Method.GET});
        Resource imageGIF = new DynamicResource(URI.create("/image.gif"), staticFilesystem, new Request.Method[] {Request.Method.GET});
        Resource textFile = new DynamicResource(URI.create("/text-file.txt"), staticFilesystem, new Request.Method[] {Request.Method.GET});
        Resource partialContent = new DynamicResource(URI.create("/partial_content.txt"), staticFilesystem, new Request.Method[] {Request.Method.GET});
        Resource patchContent = new DynamicResource(URI.create("/patch-content.txt"), staticFilesystem, new Request.Method[] {Request.Method.GET, Request.Method.PATCH});
        Resource redirect = new RedirectedResource(URI.create("/redirect"), URI.create("http://localhost:5000/"));
        Resource cookie = new DynamicResourceWithCookie(URI.create("/cookie"), dynamicFilesystem, new Request.Method[] {Request.Method.GET});
        Resource eatCookie = new DynamicResourceWithCookie(URI.create("/eat_cookie"), dynamicFilesystem, new Request.Method[] {Request.Method.GET});

        resources.addResource(root);
        resources.addResource(form);
        resources.addResource(methodOptions);
        resources.addResource(methodOptionsTwo);
        resources.addResource(tea);
        resources.addResource(parameters);
        resources.addResource(log);
        resources.addResource(these);
        resources.addResource(requests);
        resources.addResource(logs);
        resources.addResource(coffee);
        resources.addResource(fileOne);
        resources.addResource(imageJPEG);
        resources.addResource(imagePNG);
        resources.addResource(imageGIF);
        resources.addResource(textFile);
        resources.addResource(partialContent);
        resources.addResource(patchContent);
        resources.addResource(redirect);
        resources.addResource(cookie);
        resources.addResource(eatCookie);

        resources.requireAuthenticationFor(logs);

        return resources;
    }
}
