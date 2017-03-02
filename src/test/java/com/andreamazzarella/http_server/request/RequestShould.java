package com.andreamazzarella.http_server.request;

import com.andreamazzarella.http_server.headers.Header;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.andreamazzarella.http_server.headers.Header.CONTENT_LENGTH_HEADER_NAME;
import static com.andreamazzarella.http_server.headers.Header.CONTENT_TYPE_HEADER_NAME;
import static com.andreamazzarella.http_server.request.Request.Method.GET;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RequestShould {

    @Test
    public void provideTheRequestLine() {
        String requestLine = "GET /path/to/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(requestLine, request.getRequestLine());
    }
    @Test
    public void extractTheAbsolutePathRequestURIFromASimpleRequest() {
        String requestLine = "GET /path/to/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(URI.create("/path/to/resource"), request.getUri());
    }

    @Test
    public void extractTheAbsolutePathRequestURIFromARequestWithPatameters() {
        String requestLine = "GET /path/to/lol/resource?parameter=cats HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(URI.create("/path/to/lol/resource"), request.getUri());
    }

    @Test
    public void extractTheGetMethod() {
        String requestLine = "GET /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(GET, request.getMethod());
    }

    @Test
    public void extractTheHeadMethod() {
        String requestLine = "HEAD /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(Request.Method.HEAD, request.getMethod());
    }

    @Test
    public void extractThePostMethod() {
        String requestLine = "POST /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void extractThePutMethod() {
        String requestLine = "PUT /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(Request.Method.PUT, request.getMethod());
    }

    @Test
    public void extractTheOptionsMethod() {
        String requestLine = "OPTIONS /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(Request.Method.OPTIONS, request.getMethod());
    }

    @Test
    public void extractTheDeleteMethod() {
        String requestLine = "DELETE /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(Request.Method.DELETE, request.getMethod());
    }

    @Test
    public void extractThePatchMethod() {
        String requestLine = "PATCH /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(Request.Method.PATCH, request.getMethod());
    }

    @Test
    public void provideUnrecognisedMethod() {
        String requestLine = "MADE_UP_METHOD /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(Request.Method.UNRECOGNISED_METHOD, request.getMethod());
    }

    @Test
    public void extractParametersFromTheRequestLine() {
        String requestLine = "GET /path/to/lol/resource?key=value&yo=whazzaaa HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        String parameterOneValue = request.getParameter("key").get().getValue();
        String parameterTwoValue = request.getParameter("yo").get().getValue();

        assertEquals("value", parameterOneValue);
        assertEquals("whazzaaa", parameterTwoValue);
    }

    @Test
    public void handleInexistentParameters() {
        String requestLine = "GET /path/to/lol/resource?key=value HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        Optional<Parameter> parameter = request.getParameter("missing_key");

        assertEquals(Optional.empty(), parameter);
    }

    @Test
    public void provideAccessToTheContentLengthHeader() {
        String requestLine = "GET /path/to/lol/resource HTTP/1.1";
        Header expectedHeader = new Header(CONTENT_LENGTH_HEADER_NAME, "42");
        List<Header> headers = new ArrayList<>();
        headers.add(expectedHeader);
        Request request = new Request(requestLine, headers, Optional.empty());

        Optional<Header> header = request.getContentLengthHeader();

        assertEquals(CONTENT_LENGTH_HEADER_NAME, header.get().getName());
        assertEquals("42", header.get().getValue());
    }

    @Test
    public void provideAccessToTheMediaTypeHeader() {
        String requestLine = "GET /path/to/lol/resource HTTP/1.1";
        Header expectedHeader = new Header(CONTENT_TYPE_HEADER_NAME, "image/gif");
        List<Header> headers = new ArrayList<>();
        headers.add(expectedHeader);
        Request request = new Request(requestLine, headers, Optional.empty());

        Optional<Header> header = request.getContentTypeHeader();

        assertEquals(CONTENT_TYPE_HEADER_NAME, header.get().getName());
        assertEquals("image/gif", header.get().getValue());
    }

    @Test
    public void handleMissingHeaders() {
        String requestLine = "GET /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        Optional<Header> contentLengthHeader = request.getContentLengthHeader();
        Optional<Header> contentTypeHeader = request.getContentTypeHeader();

        assertEquals(Optional.empty(), contentLengthHeader);
        assertEquals(Optional.empty(), contentTypeHeader);
    }

    @Test
    public void provideTheBodyWhenPresent() {
        String requestLine = "GET /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        byte[] body = "What would Beyonce do?".getBytes();
        Request request = new Request(requestLine, emptyHeaders, Optional.of(body));

        assertEquals(Optional.of(body), request.getBody());
    }

    @Test
    public void handleMissingBody() {
        String requestLine = "GET /path/to/lol/resource HTTP/1.1";
        List<Header> emptyHeaders = new ArrayList<>();
        Request request = new Request(requestLine, emptyHeaders, Optional.empty());

        assertEquals(Optional.empty(), request.getBody());
    }

    @Test
    public void parseFromASocketConnection() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /path/to/resource?parameter1=safe&parameter2=sound HTTP/1.1\n" +
                "Content-Length: 31\n" +
                "\n" +
                "some body here for good measure");

        Request request = Request.parseFromSocket(socketConnection);

        assertEquals("GET /path/to/resource?parameter1=safe&parameter2=sound HTTP/1.1", request.getRequestLine());
        assertEquals(GET, request.getMethod());
        assertEquals("safe", request.getParameter("parameter1").get().getValue());
        assertEquals("sound", request.getParameter("parameter2").get().getValue());
        assertEquals(CONTENT_LENGTH_HEADER_NAME, request.getContentLengthHeader().get().getName());
        assertArrayEquals("some body here for good measure".getBytes(), request.getBody().get());
    }

}
