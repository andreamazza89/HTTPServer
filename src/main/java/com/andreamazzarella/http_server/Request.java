package com.andreamazzarella.http_server;

import java.net.URI;
import java.util.Hashtable;
import java.util.Map;

public class Request {



    public enum Method {
        POST, OPTIONS, DELETE, PUT, HEAD, GET
    }

    private static final int INDEX_OF_REQUEST_LINE = 0;
    private static final int INDEX_OF_REQUEST_URI = 1;
    private static final int INDEX_OF_FIELD_NAME = 0;
    private static final int INDEX_OF_FIELD_VALUE = 1;
    private static final String CARRIAGE_RETURN_LINE_FEED = "\n";
    private static final String SPACE = " ";

    private final String requestMessage;
    private final DataExchange socketConnection;
    private final String[] tokenisedRequestMessage;
    private final String requestLine;
    private final String[] tokenisedRequestLine;
    private String requestBody;
    private Map<String, String> headers = new Hashtable<>();

    Request(DataExchange socketConnection) {
        this.requestMessage = socketConnection.readLine();
        this.socketConnection = socketConnection;
        this.tokenisedRequestMessage = tokenise(requestMessage, CARRIAGE_RETURN_LINE_FEED);
        this.requestLine = tokenisedRequestMessage[INDEX_OF_REQUEST_LINE];
        this.tokenisedRequestLine = tokenise(requestLine, SPACE);

        parseHeaders();
        parseBody();
    }

    private void parseHeaders() {
        while (true) {
            String headerOrEndOFHeaders = socketConnection.readLine();
            if (headerOrEndOFHeaders.matches("")) {
                break;
            } else {
                String[] tokenisedHeader = tokenise(headerOrEndOFHeaders, SPACE);
                headers.put(tokenisedHeader[INDEX_OF_FIELD_NAME], tokenisedHeader[INDEX_OF_FIELD_VALUE]);
            }
        }
    }

    private void parseBody() {
        if (headers.containsKey("Content-Length:")) {
            int contentLenth = Integer.parseInt(headers.get("Content-Length:"));
            char[] buffer = new char[contentLenth];
            socketConnection.read(buffer, 0, contentLenth);
            requestBody = String.valueOf(buffer);
        }
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

    String getHeader(String fieldName) {
        return headers.get(fieldName + ":");
    }

    URI uri() {
        return URI.create(tokenisedRequestLine[INDEX_OF_REQUEST_URI]);
    }

    String body() {
        return requestBody;
    }

    private String[] tokenise(String joinedTokens, String separator) {
        return joinedTokens.split(separator);
    }

}
