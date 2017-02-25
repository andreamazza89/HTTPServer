package com.andreamazzarella.http_server;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

//public class DynamicResourceShould {
//
//    @Test
//    public void reportOnWhichMethodsAreAllowed() {
//        URI resourcePath = URI.create("/");
//        DynamicResource resource = new DynamicResource(resourcePath);
//        resource.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.POST});
//
//        assertArrayEquals(new Request.Method[] {Request.Method.GET, Request.Method.POST}, resource.methodsAllowed());
//    }
//
//    @Test
//    public void knowIfTheResourceIsNotToBeRedirected() {
//        URI resourcePath = URI.create("/no_redirect");
//        DynamicResource resource = new DynamicResource(resourcePath);
//
//        assertEquals(false, resource.isRedirect());
//    }
//
//    @Test
//    public void allowTheResourceToBeRedirected() {
//        URI resourcePath = URI.create("/redirect");
//        DynamicResource resource = new DynamicResource(resourcePath);
//        resource.allowMethods(new Request.Method[] {Request.Method.GET});
//        resource.setRedirect(URI.create("/go/to/this/uri"));
//
//        assertEquals(true, resource.isRedirect());
//        assertEquals("/go/to/this/uri", resource.redirectLocation().getPath());
//    }
//
//    @Test
//    public void knowIfTheResourceIsNotATeaPot() {
//        URI resourcePath = URI.create("/no_tea_for_you");
//        DynamicResource resource = new DynamicResource(resourcePath);
//
//        assertEquals(false, resource.isTeaPot());
//    }
//
//    @Test
//    public void knowIfTheResourceIsATeaPot() {
//        URI resourcePath = URI.create("/no_tea_for_you");
//        DynamicResource resource = new DynamicResource(resourcePath);
//        resource.setTeaPot();
//
//        assertEquals(true, resource.isTeaPot());
//    }
//
//    @Test
//    public void getTheResourceContent() {
//        URI resourcePath = URI.create("/the_resource_I_want_is_here");
//        FileSystem blaah = new FakeFileSystem(URI.create("./resources/"));
//        blaah.addOrReplaceResource(resourcePath, "Hello, is it me you are looking for");
//        DynamicResource resource = new DynamicResource(resourcePath, blaah);
//
//        FakeSocketConnection socketConnection = new FakeSocketConnection();
//        socketConnection.setRequestTo("GET " + resourcePath +"HTTP/1.1\n\n");
//        Request request = new Request(socketConnection);
//
//        resource.execute(request);
//
//        assertEquals("Hello, is it me you are looking for", resource.getContent());
//
//        blaah.deleteResource(resourcePath);
//    }
//
//    @Test
//    public void postContentToAResource() {
//        URI resourcePath = URI.create("/the_resource_I_want_is_here");
//        FileSystem blaah = new FakeFileSystem(URI.create("./resources/"));
//        DynamicResource resource = new DynamicResource(resourcePath, blaah);
//
//        FakeSocketConnection socketConnection = new FakeSocketConnection();
//        String messageBody = "Mariah Carey";
//        socketConnection.setRequestTo("POST " + resourcePath + "HTTP/1.1\nContent-Length: "
//                + messageBody.getBytes().length + "\n\n" + messageBody);
//        Request request = new Request(socketConnection);
//
//        resource.execute(request);
//
//        assertEquals(blaah.getResource(resourcePath).get(), messageBody);
//
//        blaah.deleteResource(resourcePath);
//    }
//
//    @Test
//    public void putContentToAResource() {
//        URI resourcePath = URI.create("/the_resource_I_want_is_here");
//        FileSystem blaah = new FakeFileSystem(URI.create("./resources/"));
//        DynamicResource resource = new DynamicResource(resourcePath, blaah);
//
//        FakeSocketConnection socketConnection = new FakeSocketConnection();
//        String messageBody = "Mariah Carey";
//        socketConnection.setRequestTo("PUT " + resourcePath + "HTTP/1.1\nContent-Length: "
//                + messageBody.getBytes().length + "\n\n" + messageBody);
//        Request request = new Request(socketConnection);
//
//        resource.execute(request);
//
//        assertEquals(blaah.getResource(resourcePath).get(), messageBody);
//
//        blaah.deleteResource(resourcePath);
//    }
//
//    @Test
//    public void deleteAResource() {
//        URI resourcePath = URI.create("/the_resource_I_want_is_here");
//        FileSystem blaah = new FakeFileSystem(URI.create("./resources/"));
//        blaah.addOrReplaceResource(resourcePath, "Please destroy the evidence");
//        DynamicResource resource = new DynamicResource(resourcePath, blaah);
//
//        FakeSocketConnection socketConnection = new FakeSocketConnection();
//        socketConnection.setRequestTo("DELETE " + resourcePath + "HTTP/1.1\n\n");
//        Request request = new Request(socketConnection);
//
//        resource.execute(request);
//
//        assertEquals(blaah.getResource(resourcePath), Optional.empty());
//
//        blaah.deleteResource(resourcePath);
//    }
//
//    @Test
//    public void provideOptionsForAResourceInTheHeaders() {
//        URI resourcePath = URI.create("/the_resource_I_want_is_here");
//        FileSystem blaah = new FakeFileSystem(URI.create("./resources/"));
//        blaah.addOrReplaceResource(resourcePath, "Please destroy the evidence");
//        DynamicResource resource = new DynamicResource(resourcePath, blaah);
//        resource.allowMethods(new Request.Method[] {Request.Method.GET, Request.Method.OPTIONS});
//
//        FakeSocketConnection socketConnection = new FakeSocketConnection();
//        socketConnection.setRequestTo("OPTIONS " + resourcePath + "HTTP/1.1\n\n");
//        Request request = new Request(socketConnection);
//
//        resource.execute(request);
//
//        assertEquals("Allow: GET,OPTIONS\n", resource.getResponseHeaders());
//    }
//}
