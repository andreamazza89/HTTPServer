package com.andreamazzarella.http_server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Response {

    private final String END_OF_HEADERS = "\n";

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
    private final List<Header> headers = new ArrayList<>();
    private Optional<byte[]> body = Optional.empty();

    public Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Response setBody(byte[] body) {
        this.body =  Optional.of(body);
        return this;
    }

    public Response setBody(String body) {
        return setBody(body.getBytes());
    }

    public Optional<byte[]> getBody() {
        return body;
    }

    public Response addHeader(Header header) {
        headers.add(header);
        return this;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    byte[] toByteArray() {
        String headers = serialiseHeaders();
        byte[] statusAndHeaders = (statusCode.getStatusLine() + headers + END_OF_HEADERS).getBytes();
        return ArrayOperations.concatenateData(statusAndHeaders, body.orElse("".getBytes()));
    }

    private String serialiseHeaders() {
        String serialisedHeaders = "";

        for (Header header : headers) {
            serialisedHeaders += header.toString();
        }

        return serialisedHeaders;
    }
}
