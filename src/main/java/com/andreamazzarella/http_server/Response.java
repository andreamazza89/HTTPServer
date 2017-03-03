package com.andreamazzarella.http_server;

public class Response {

    public final String END_OF_HEADERS = "\n";

    public enum StatusCode {
        _200("HTTP/1.1 200 OK\n"),
        _401("HTTP/1.1 401 Unauthorized\n"),
        _404("HTTP/1.1 404 Not Found\n"),
        _418("HTTP/1.1 418 I'm a teapot\n");

        private final String status;

        StatusCode(String status) {
            this.status = status;
        }

        public String getStatusLine() {
            return status;
        }
    }


    private final StatusCode statusCode;
    private byte[] body;

    public Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setBody(byte[] body) {
        this.body =  body;
    }

    public byte[] body() {
        return body;
    }

    byte[] toByteArray() {
        return (statusCode.getStatusLine() + END_OF_HEADERS).getBytes();
    }
}
