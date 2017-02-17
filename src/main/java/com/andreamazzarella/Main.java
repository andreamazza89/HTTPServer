package com.andreamazzarella;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        InputParser inputParser = new InputParser(args);
        int portNumber = inputParser.parsePortNumber();
        String publicDirectory = inputParser.parseDirectoryPath();

        HTTPServer server = new HTTPServer(portNumber, publicDirectory);
        server.start();

//        Socket socket = new ServerSocket(portNumber).accept();
//        new PrintStream(socket.getOutputStream()).println("HTTP/1.1 200 OK\n");
    }
}
