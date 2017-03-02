package com.andreamazzarella.http_server.request;

public class Parameter {
    private final String key;
    private String value;

    public Parameter(String key, String value) {
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
