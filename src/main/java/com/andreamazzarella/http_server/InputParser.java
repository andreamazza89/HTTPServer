package com.andreamazzarella.http_server;

class InputParser {
    private static final int DEFAULT_PORT_NUMBER = 5000;

    private String[] args;

    InputParser(String[] args) {
        this.args = args;
    }

    int parsePortNumber() {
        int portNumberIndex = findIndexOfFlag("-p");
        return portNumberIndex == -1 ?  DEFAULT_PORT_NUMBER : Integer.parseInt(args[portNumberIndex + 1]);
    }

    String parseDirectoryPath() {
        int directoryPathIndex = findIndexOfFlag("-d");
        return args[directoryPathIndex + 1];
    }

    private int findIndexOfFlag(String flag) {
        int flagIndex = -1;
        int currentIndex = 0;
        for (String token : args) {
            if (token.equals(flag)) {
                flagIndex = currentIndex;
            }
            currentIndex++;
        }
        return flagIndex;
    }
}
