package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.ArrayOperations;
import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.headers.Header;
import com.andreamazzarella.http_server.request.Request;

import java.net.URI;
import java.util.Optional;
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
        Header dataRangeHeader = request.getRangeHeader().orElse(null);
        String dataRange = dataRangeHeader == null ? null : dataRangeHeader.getValue();
        byte[] status;
        byte[] setCookieHeader = generateCookieHeader(request);
        byte[] body = fileSystem.getResource(request.getUri(), dataRange).orElse("".getBytes());
        byte[] cookie = getCookie(request);
        if (dataRange == null) {
            status = (Response.STATUS_TWO_HUNDRED).getBytes();
        } else {
            status = (Response.STATUS_TWO_OH_SIX).getBytes();
        }
        return ArrayOperations.concatenateData(status, setCookieHeader, Response.END_OF_HEADERS.getBytes(), body, cookie);
    }

    private byte[] getCookie(Request request) {
        Optional<Header> cookie = request.getCookieHeader();
        if (cookie.isPresent()) {
            return cookie.get().getValue().split("=")[1].getBytes();
        } else {
            return "".getBytes();
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
