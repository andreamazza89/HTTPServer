package com.andreamazzarella.http_server.com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.ArrayOperations;
import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.Request;

import java.net.URI;
import java.util.Set;

public class DynamicResourceWithCookie implements Resource {

    private final URI uri;
    private final FileSystem fileSystem;

    public DynamicResourceWithCookie(URI uri, FileSystem fileSystem, Request.Method[] methods) {
        this.uri = uri;
        this.fileSystem = fileSystem;
    }

    @Override
    public byte[] generateResponse(Request request) {
        String dataRange = request.getHeader("Range");
        byte[] status;
        byte[] setCookieHeader = generateCookieHeader(request);
        byte[] body = fileSystem.getResource(request.uri(), dataRange).orElse("".getBytes());
        byte[] cookie = getCookie(request);
        if (dataRange == null) {
            status = (Response.STATUS_TWO_HUNDRED).getBytes();
        } else {
            status = (Response.STATUS_TWO_OH_SIX).getBytes();
        }
        return ArrayOperations.concatenateData(status, setCookieHeader, Response.END_OF_HEADERS.getBytes(), body, cookie);
    }

    private byte[] getCookie(Request request) {
        String cookie = request.getHeader("Cookie");
        if (cookie == null) {
            return "".getBytes();
        } else {
            return cookie.split("=")[1].getBytes();
        }
    }

    private byte[] generateCookieHeader(Request request) {
        Set<String> parameterKeys = request.getParams().keySet();

        String cookieHeader = "Set-Cookie: ";
        for (String parameterKey : parameterKeys) {
            cookieHeader += parameterKey + "=" + request.getParams().get(parameterKey);
        }
        return (cookieHeader + Response.NEWLINE).getBytes();
    }

    @Override
    public URI uri() {
        return uri;
    }
}
