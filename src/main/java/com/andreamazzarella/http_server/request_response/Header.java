package com.andreamazzarella.http_server.request_response;

public class Header {

    public static final String CONTENT_LENGTH_HEADER_NAME = "Content-Length";
    public static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String RANGE_HEADER_NAME = "Range";
    public static final String COOKIE_HEADER_NAME = "Cookie";
    public static final String SET_COOKIE_HEADER_NAME = "Set-Cookie";
    public static final String REDIRECT_HEADER_NAME = "Location";
    public static final String IF_MATCH_HEADER_NAME = "If-Match";

    private final String name;
    private final String value;

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s\n", name, value);
    }
}
