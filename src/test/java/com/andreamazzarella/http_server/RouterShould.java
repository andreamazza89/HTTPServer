package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.support.StubbedRoutes;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RouterShould {

    @Test
    public void forwardTheGeneratedResponseToTheClient() {
        Routes routes = new StubbedRoutes();
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("double request message\n\n");
        Router router = new Router(socketConnection, routes);

        router.respondToClient();

        assertEquals("stubbed response", socketConnection.messageReceived());
    }

}
