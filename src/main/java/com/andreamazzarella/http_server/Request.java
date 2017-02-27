package com.andreamazzarella.http_server;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {

    public enum Method {
        POST, OPTIONS, DELETE, PUT, HEAD, UNRECOGNISED_METHOD, PATCH, GET
    }

    private static final int INDEX_OF_REQUEST_LINE = 0;
    private static final int INDEX_OF_REQUEST_URI = 1;
    private static final int INDEX_OF_FIELD_NAME = 0;
    private static final int INDEX_OF_FIELD_VALUE = 1;
    private static final String CARRIAGE_RETURN_LINE_FEED = "\n";
    private static final String SPACE = " ";

    private final Map<String, String> queryParameters = new TreeMap<>();
    private final String requestMessage;
    private final DataExchange socketConnection;
    private final String[] tokenisedRequestMessage;
    private final String requestLine;
    private final String[] tokenisedRequestLine;
    private String requestBody;
    private Map<String, String> headers = new Hashtable<>();

    protected Request(DataExchange socketConnection) {
        this.requestMessage = socketConnection.readLine();
        this.socketConnection = socketConnection;
        this.tokenisedRequestMessage = tokenise(requestMessage, CARRIAGE_RETURN_LINE_FEED);
        this.requestLine = tokenisedRequestMessage[INDEX_OF_REQUEST_LINE];
        this.tokenisedRequestLine = tokenise(requestLine, SPACE);

        parseHeaders();
        parseParameters();
        parseBody();
    }

    private void parseParameters() {
        if (URI.create(tokenisedRequestLine[INDEX_OF_REQUEST_URI]).getQuery() != null) {
            String[] parameters = URI.create(tokenisedRequestLine[INDEX_OF_REQUEST_URI]).getRawQuery().split("&");
            for (String parameter : parameters) {
                String[] blah = parameter.split("=");
                try {
                    queryParameters.put(URLDecoder.decode(blah[0], "UTF-8"), URLDecoder.decode(blah[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseHeaders() {
        while (true) {
            String headerOrEndOFHeaders = socketConnection.readLine();
            if (headerOrEndOFHeaders.matches("")) {
                break;
            } else {
                Pattern headerField = Pattern.compile("(.+):\\s(.*)");
                Matcher matcher = headerField.matcher(headerOrEndOFHeaders);
                matcher.matches();
                headers.put(matcher.group(1), matcher.group(2));
            }
        }
    }

    private void parseBody() {
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[contentLength];
            socketConnection.read(buffer, 0, contentLength);
            requestBody = String.valueOf(buffer);
        }
    }

    String getRequestLine() {
        return requestLine;
    }

    public Method method() {
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
            case "PATCH":
                return Method.PATCH;
            default:
                return Method.UNRECOGNISED_METHOD;
        }
    }

    public String getHeader(String fieldName) {
        return headers.get(fieldName);
    }

    public Map<String,String> getParams() {
        return queryParameters;
    }

    public URI uri() {
        return URI.create(tokenisedRequestLine[INDEX_OF_REQUEST_URI]);
    }

    public String body() {
        return requestBody;
    }

    private String[] tokenise(String joinedTokens, String separator) {
        return joinedTokens.split(separator);
    }

}
