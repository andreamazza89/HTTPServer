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
    public void respondWith405WhenMethodIsNotAllowed() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("DELETE /get_only_route HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource resource = new Resource(URI.create("/get_only_route"));
        resource.allowMethods(new Request.Method[] {Request.Method.GET});

        String response = ResponseGenerator.createResponse(request, resource);

        assertEquals("HTTP/1.1 405 Not Allowed\n\n", response);
    }

    @Test
    public void respondWithATeaPotMessageForATeaPotResource() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /teapot HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource resource = new Resource(URI.create("/teapot"));
        resource.allowMethods(new Request.Method[] {Request.Method.GET});
        resource.setTeaPot();

        String response = ResponseGenerator.createResponse(request, resource);

        assertEquals("HTTP/1.1 418 I'm a Teapot\n\nI'm a teapot", response);
    }

    @Test
    public void respondWithRedirectWhenResourceIsRedirect() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /redirect HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource resource = new Resource(URI.create("/redirect"));
        resource.allowMethods(new Request.Method[] {Request.Method.GET});
        resource.setRedirect(URI.create("/go/here"));

        String response = ResponseGenerator.createResponse(request, resource);

        assertEquals("HTTP/1.1 302 Found\nLocation: /go/here\n\n", response);
    }

    @Test
    public void respondWithA200ToASimpleGet() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource resource = new Resource(URI.create("/"));
        resource.allowMethods(new Request.Method[] {Request.Method.GET});

        String response = ResponseGenerator.createResponse(request, resource);

        assertEquals("HTTP/1.1 200 OK\n\n", response);
    }

    @Test
    public void includeResourceContentInBodyIfExisting() throws IOException {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /form HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource route = new Resource(URI.create("/form"));
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
        Resource resource = new Resource(URI.create("/form"));
        resource.allowMethods(new Request.Method[] {Request.Method.POST});

        String response = ResponseGenerator.createResponse(request, resource);
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
        Resource resource = new MissingResource(URI.create("i/do/not/exist"));

        String response = ResponseGenerator.createResponse(request, resource);

        assertEquals("HTTP/1.1 404 Not Found\n\n", response);
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleOne() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource resource = new Resource(URI.create("/form"));
        resource.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.HEAD, Request.Method.POST,
                Request.Method.OPTIONS, Request.Method.PUT});

        String response = ResponseGenerator.createResponse(request, resource);

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,HEAD,POST,OPTIONS,PUT\n\n", response);
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options2 HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource resource = new Resource(URI.create("/form"));
        resource.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});

        String response = ResponseGenerator.createResponse(request, resource);

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,OPTIONS\n\n", response);
    }

    @Test
    public void provideLocationForRedirect() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /redirect HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource resource = new Resource(URI.create("/redirect"));
        resource.allowMethods(new Request.Method[] {Request.Method.GET});
        resource.setRedirect(URI.create("http://localhost:5000/"));

        String response = ResponseGenerator.createResponse(request, resource);

        assertEquals("HTTP/1.1 302 Found\nLocation: http://localhost:5000/\n\n", response);
    }

}
