package com.andreamazzarella.http_server;

public class Request {


    public enum Method {
        POST, OPTIONS, DELETE, PUT, HEAD, GET
    }

    private static final int INDEX_OF_REQUEST_LINE = 0;
    private static final int INDEX_OF_REQUEST_URI = 1;
    private static final String CARRIAGE_RETURN_LINE_FEED = "\n";
    private static final String SPACE = " ";

    private final String requestMessage;
    private final String[] tokenisedRequestMessage;
    private final String requestLine;
    private final String[] tokenisedRequestLine;

    Request(DataExchange socketConnection) {
        this.requestMessage = socketConnection.readLine();
        this.tokenisedRequestMessage = tokenise(requestMessage, CARRIAGE_RETURN_LINE_FEED);
        this.requestLine = tokenisedRequestMessage[INDEX_OF_REQUEST_LINE];
        this.tokenisedRequestLine = tokenise(requestLine, SPACE);
    }

    Method method() {
        switch (tokenisedRequestLine[0]) {
            case "GET":
                return Method.GET;
            case "HEAD":
                return Method.HEAD;
            case "POST":
                return Method.POST;
            case "PUT":
                return Method.PUT;
            case "OPTIONS":
                return Method.OPTIONS;
            case "DELETE":
                return Method.DELETE;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return requestMessage;
    }

    String extractRequestURI() {
        return tokenisedRequestLine[INDEX_OF_REQUEST_URI];
    }

    private String[] tokenise(String joinedTokens, String separator) {
        return joinedTokens.split(separator);
    }

}
