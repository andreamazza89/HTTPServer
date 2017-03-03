package com.andreamazzarella.http_server;

public class MWResponse {
    private final int statusCode;
    private byte[] body;

    public MWResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setBody(byte[] body) {
        this.body =  body;
    }

    public byte[] body() {
        return body;
    }
}
