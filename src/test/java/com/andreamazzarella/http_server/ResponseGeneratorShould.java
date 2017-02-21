package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import static org.junit.Assert.assertEquals;

public class ResponseGeneratorShould {

    @Test
    public void respondWithA200ToASimpleGet() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Route route = new Route(URI.create("/"));
        route.allowMethods(new Request.Method[] {Request.Method.GET});

        String response = ResponseGenerator.createResponse(request, route);

        assertEquals("HTTP/1.1 200 OK\n\n", response);
    }

    @Test
    public void includeResourceContentInBodyIfExisting() throws IOException {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /form HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Route route = new Route(URI.create("/form"));
        route.allowMethods(new Request.Method[] {Request.Method.GET});

        File resource = new File("resources/form");
        String path = resource.getCanonicalPath();
        PrintWriter printWriter = new PrintWriter(path);
        printWriter.print("resource data");
        printWriter.close();


        String response = ResponseGenerator.createResponse(request, route);

        assertEquals("HTTP/1.1 200 OK\n\nresource data", response);

        new File(path).delete();
    }

    @Test
    public void createAResourceWithTheGivenData() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        String messageBody = "ciao";
        socketConnection.setRequestTo("POST /form HTTP/1.1\nContent-Length: " + messageBody.getBytes().length + "\n\n" + messageBody);
        Request request = new Request(socketConnection);
        Route route = new Route(URI.create("/form"));
        route.allowMethods(new Request.Method[] {Request.Method.POST});

        String response = ResponseGenerator.createResponse(request, route);
        assertEquals("HTTP/1.1 200 OK\n\n", response);

        File file = new File("resources/form");
        FileInputStream fis;
        byte[] data = new byte[(int) file.length()];
        try {
            fis = new FileInputStream(file);
            fis.read(data);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("ciao", new String(data));

        file.delete();
    }

    @Test
    public void respondWith404WhenResourceDoesNotExist() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("HEAD /foobar HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Route route = new MissingRoute(URI.create("i/do/not/exist"));

        String response = ResponseGenerator.createResponse(request, route);

        assertEquals("HTTP/1.1 404 Not Found\n\n", response);
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleOne() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Route route = new Route(URI.create("/form"));
        route.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.HEAD, Request.Method.POST,
                Request.Method.OPTIONS, Request.Method.PUT});

        String response = ResponseGenerator.createResponse(request, route);

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,HEAD,POST,OPTIONS,PUT\n\n", response);
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options2 HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Route route = new Route(URI.create("/form"));
        route.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});

        String response = ResponseGenerator.createResponse(request, route);

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,OPTIONS\n\n", response);
    }

    @Test
    public void provideLocationForRedirect() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /redirect HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Route route = new Route(URI.create("/redirect"));
        route.allowMethods(new Request.Method[] {Request.Method.GET});
        route.setRedirect(URI.create("http://localhost:5000/"));

        String response = ResponseGenerator.createResponse(request, route);

        assertEquals("HTTP/1.1 302 Found\nLocation: http://localhost:5000/\n\n", response);
    }

}
