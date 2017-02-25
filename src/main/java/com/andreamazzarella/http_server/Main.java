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
        URI publicPath = URI.create("/Users/Andrea/Dropbox/Programming/repos/java/cob_spec/public/");

        Resources resources = new Resources();
        FileSystem staticFilesystem = new FileSystem(publicPath);
        DirectoryExplorer directoryExplorer = new DirectoryExplorer(publicPath);

        Resource teapot = new TeaPotResource(URI.create("/coffee"));
        Resource root = new IndexResource(URI.create("/"), directoryExplorer);
        Resource fileOne = new StaticResource(URI.create("/file1"), staticFilesystem);
        Resource imageJPEG = new StaticResource(URI.create("/image.jpeg"), staticFilesystem);
        Resource imagePNG = new StaticResource(URI.create("/image.png"), staticFilesystem);
        Resource imageGIF = new StaticResource(URI.create("/image.gif"), staticFilesystem);
        Resource textFile = new StaticResource(URI.create("/text-file.txt"), staticFilesystem);
        Resource redirect = new RedirectedResource(URI.create("/redirect"), URI.create("http://localhost:5000/"));

        resources.addResource(teapot);
        resources.addResource(root);
        resources.addResource(fileOne);
        resources.addResource(imageJPEG);
        resources.addResource(imagePNG);
        resources.addResource(imageGIF);
        resources.addResource(textFile);
        resources.addResource(redirect);

        return resources;
    }
}
