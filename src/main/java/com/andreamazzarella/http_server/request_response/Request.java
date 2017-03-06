package com.andreamazzarella.http_server.request_response;

import com.andreamazzarella.http_server.socket_connection.DataExchange;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.andreamazzarella.http_server.request_response.Header.*;

public class Request {

    public static Request parseFromSocket(DataExchange socketConnection) {
        String requestLine = socketConnection.readLine().trim();
        List<Header> headers = parseHeaders(socketConnection);
        Optional<byte[]> body = parseBody(socketConnection, headers);

        return new Request(requestLine, headers, body);
    }

    private static List<Header> parseHeaders(DataExchange socketConnection) {
        List<Header> headers = new ArrayList<>();
        while (true) {
            String headerOrEndOFHeaders = socketConnection.readLine();
            if (headerOrEndOFHeaders.matches(END_OF_HEADERS)) {
                break;
            } else {
                Pattern headerPattern = Pattern.compile("(?<headerName>.+):\\s(?<headerValue>.*)");
                Matcher matcher = headerPattern.matcher(headerOrEndOFHeaders);
                matcher.matches();
                headers.add(new Header(matcher.group("headerName"), matcher.group("headerValue")));
            }
        }

        return headers;
    }

    private static Optional<byte[]> parseBody(DataExchange socketConnection, List<Header> headers) {
        Optional<Header> headerFound = searchHeaderByName(headers, CONTENT_LENGTH_HEADER_NAME);
        if (headerFound.isPresent()) {
            int contentLength = Integer.parseInt(headerFound.get().getValue());
            byte[] buffer = new byte[contentLength];
            socketConnection.read(buffer, 0, contentLength);
            return Optional.of(buffer);
        } else {
            return Optional.empty();
        }
    }

    public Request(String requestLine, List<Header> headers, Optional<byte[]> body) {
        this.requestLine = requestLine;
        this.parameters = parseParameters(requestLine);
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getParams() {
        Map<String, String> params = new TreeMap<>();
        for (Optional<Parameter> optionalParameter : parameters) {
            Parameter parameter = optionalParameter.get();
            params.put(parameter.getKey(), parameter.getValue());
        }
        return params;
    }

    public enum Method {
        POST, OPTIONS, DELETE, PUT, HEAD, UNRECOGNISED_METHOD, PATCH, GET
    }

    private static final String END_OF_HEADERS = "";

    private final String requestLine;
    private List<Header> headers;
    private Optional<byte[]> body;
    private List<Optional<Parameter>> parameters = new ArrayList<>();

    public URI getUri() {
        String uriWithoutParams = extractUri(requestLine).getPath();
        return URI.create(uriWithoutParams);
    }

    public String getRequestLine() {
        return requestLine;
    }

    public Request.Method getMethod() {
        Pattern requestMethodPattern = Pattern.compile("^(?<requestMethod>\\w+)\\s.*$");
        Matcher matcher = requestMethodPattern.matcher(requestLine);
        matcher.matches();

        switch (matcher.group("requestMethod")) {
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

    public Optional<Parameter> getParameter(String key) {
       return parameters.stream()
                .flatMap((optionalParameter) -> Stream.of(optionalParameter.get()))
                .filter((parameter) -> parameter.getKey().equals(key))
                .findFirst();
    }

    public Optional<Header> getContentLengthHeader() {
        return searchHeaderByName(headers, CONTENT_LENGTH_HEADER_NAME);
    }

    public Optional<Header> getContentTypeHeader() {
        return searchHeaderByName(headers, CONTENT_TYPE_HEADER_NAME);
    }

    public Optional<Header> getAuthorizationHeader() {
        return searchHeaderByName(headers, AUTHORIZATION_HEADER_NAME);
    }

    public Optional<Header> getRangeHeader() {
        return searchHeaderByName(headers, RANGE_HEADER_NAME);
    }

    public Optional<Header> getCookieHeader() {
        return searchHeaderByName(headers, COOKIE_HEADER_NAME);
    }

    public Optional<Header> getIfMatchHeader() {
        return searchHeaderByName(headers, IF_MATCH_HEADER_NAME);
    }

    private static Optional<Header> searchHeaderByName(List<Header> headers, String name) {
        return headers.stream()
                .filter((header) -> header.getName().equals(name))
                .findFirst();
    }

    public Optional<byte[]> getBody() {
        return body;
    }

    private List<Optional<Parameter>> parseParameters(String requestLine) {
        List<Optional<Parameter>> parametersParsed = new ArrayList<>();
        String urlEncodedRequestQuery = extractUri(requestLine).getRawQuery();

        if (urlEncodedRequestQuery != null) {
            String[] parameters = urlEncodedRequestQuery.split("&");
            for (String parameter : parameters) {
                String urlEncodedKey = parameter.split("=")[0];
                String urlEncodedValue = parameter.split("=")[1];
                Optional<Parameter>  newParameter = Optional.of(new Parameter(urlDecode(urlEncodedKey), urlDecode(urlEncodedValue)));
                parametersParsed.add(newParameter);
            }
        }

        return parametersParsed;
    }

    private URI extractUri(String requestLine) {
        Pattern requestPathPattern = Pattern.compile("^.*\\s(?<uriWithParams>.+)\\s.*$");
        Matcher matcher = requestPathPattern.matcher(requestLine);
        matcher.matches();
        return URI.create(matcher.group("uriWithParams"));
    }

    private String urlDecode(String encodedText) {
        try {
            return URLDecoder.decode(encodedText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("invalid URL encoding");
        }
    }
}
