package com.andreamazzarella.http_server;

public class MWResponse {
    private final int statusCode;

    public MWResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
