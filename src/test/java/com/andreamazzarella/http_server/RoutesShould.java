package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URI;

import static junit.framework.TestCase.assertEquals;

public class RoutesShould {

    private static final Routes routes = new Routes();

    @Before
    public void addRoutes() {
        Route root = new Route(URI.create("/"));
        Route methodOptions = new Route(URI.create("/method_options"));
        Route methodOptionsTwo = new Route(URI.create("/method_options2"));
        Route redirect = new Route(URI.create("/redirect"));
        Route form = new Route(URI.create("/form"));

        root.allowMethods(new Request.Method[] {Request.Method.GET});
        methodOptions.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.HEAD,
                Request.Method.POST, Request.Method.OPTIONS, Request.Method.PUT});
        methodOptionsTwo.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});
        redirect.allowMethods(new Request.Method[] {Request.Method.GET});
        form.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.POST});

        redirect.setRedirect(URI.create("http://localhost:5000/"));

        routes.addRoute(root);
        routes.addRoute(methodOptions);
        routes.addRoute(methodOptionsTwo);
        routes.addRoute(redirect);
        routes.addRoute(form);
    }

    @Test
    public void respondWithA200ToASimpleGet() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET / HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 200 OK\n\n", response);
    }

    @Test
    public void includeResourceContentInBodyIfExisting() throws IOException {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /form HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        File resource = new File("resources/form");
        String path = resource.getCanonicalPath();
        PrintWriter printWriter = new PrintWriter(path);
        printWriter.print("resource data");
        printWriter.close();

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 200 OK\n\nresource data", response);

        new File(path).delete();
    }

    @Test
    public void createAResourceWithTheGivenData() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        String messageBody = "ciao";
        socketConnection.setRequestTo("POST /form HTTP/1.1\nContent-Length: " + messageBody.getBytes().length + "\n\n" + messageBody);
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);
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

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 404 Not Found\n\n", response);
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleOne() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,HEAD,POST,OPTIONS,PUT\n\n", response);
    }

    @Test
    public void includeAllowHeaderWhenRequestMethodIsOPTIONSExampleTwo() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("OPTIONS /method_options2 HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 200 OK\nAllow: GET,OPTIONS\n\n", response);
    }

    @Test
    public void provideLocationForRedirect() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /redirect HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        String response = routes.generateResponse(request);

        assertEquals("HTTP/1.1 302 Found\nLocation: http://localhost:5000/\n\n", response);
    }



}
