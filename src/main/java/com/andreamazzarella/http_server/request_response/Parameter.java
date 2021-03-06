package com.andreamazzarella.http_server.request_response;

public class Parameter {
    private final String key;
    private String value;

    Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
