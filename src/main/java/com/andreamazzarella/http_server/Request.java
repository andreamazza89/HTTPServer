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

    private static final int INDEX_OF_REQUEST_METHOD = 0;
    private static final int INDEX_OF_REQUEST_URI = 1;
    private static final String SPACE = " ";
    private static final String END_OF_HEADERS = "";

    private final DataExchange socketConnection;

    private final String requestLine;
    private final Method requestMethod;
    private final Map<String, String> queryParameters = new TreeMap<>();
    private final Map<String, String> headers = new Hashtable<>();
    private final String requestBody;

    public Request(DataExchange socketConnection) {
        this.socketConnection = socketConnection;

        this.requestLine = socketConnection.readLine().trim();
        this.requestMethod = selectMethod();

        parseParameters();
        parseHeaders();

        this.requestBody = parseBody();
    }

    String getRequestLine() {
        return requestLine;
    }

    public Method getMethod() {
        return requestMethod;
    }

    public URI getUri() {
        return URI.create(requestLine.split(SPACE)[INDEX_OF_REQUEST_URI]);
    }

    public Map<String,String> getParams() {
        return queryParameters;
    }

    public String getHeader(String fieldName) {
        return headers.get(fieldName);
    }

    public String getBody() {
        return requestBody;
    }

    private Method selectMethod() {
        String requestMethod = requestLine.split(SPACE)[INDEX_OF_REQUEST_METHOD];

        switch (requestMethod) {
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

    private void parseParameters() {
        URI requestUri = URI.create(requestLine.split(SPACE)[INDEX_OF_REQUEST_URI]);
        String urlEncodedRequestQuery = requestUri.getRawQuery();
        if (urlEncodedRequestQuery != null) {
            String[] parameters = urlEncodedRequestQuery.split("&");
            for (String parameter : parameters) {
                String urlEncodedKey = parameter.split("=")[0];
                String urlEncodedValue = parameter.split("=")[1];
                queryParameters.put(urlDecode(urlEncodedKey), urlDecode(urlEncodedValue));
            }
        }
    }

    private String urlDecode(String encodedText) {
        try {
            return URLDecoder.decode(encodedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("invalid URL encoding");
        }
    }

    private void parseHeaders() {
        while (true) {
            String headerOrEndOFHeaders = socketConnection.readLine();
            if (headerOrEndOFHeaders.matches(END_OF_HEADERS)) {
                break;
            } else {
                Pattern header = Pattern.compile("(?<headerName>.+):\\s(?<headerValue>.*)");
                Matcher matcher = header.matcher(headerOrEndOFHeaders);
                matcher.matches();
                headers.put(matcher.group("headerName"), matcher.group("headerValue"));
            }
        }
    }

    private String parseBody() {
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[contentLength];
            socketConnection.read(buffer, 0, contentLength);
            return String.valueOf(buffer);
        } else {
            return null;
        }
    }
}
