package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.middleware.MiddleWare;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        InputParser inputParser = new InputParser(args);
        int portNumber = inputParser.parsePortNumber();

        String publicDirectory = inputParser.parseDirectoryPath();
        String loggingDirectory = "./logs/";

        MiddleWare middleWareStack = Configuration.createMiddleWareStack(publicDirectory, loggingDirectory);

        HTTPServer server = new HTTPServer(portNumber, middleWareStack);
        System.out.println("Server running on port: " + portNumber);
        server.start();
    }

}
