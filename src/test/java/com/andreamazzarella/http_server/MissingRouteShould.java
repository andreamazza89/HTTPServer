package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertArrayEquals;

public class MissingRouteShould {

    @Test
    public void onlyAllowPutRequests() {
        Resource resource = new MissingResource(URI.create("/missing/resource"));
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("\n\n");
        Request.Method[] methodsAllowed = resource.methodsAllowed();
        assertArrayEquals(new Request.Method[]{Request.Method.PUT}, methodsAllowed);
    }
}
