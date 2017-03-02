package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnauthorisedResourceShould {

    @Test
    public void provideAFourOhOneResponse() {
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("POST /cant_touch_this HTTP/1.1\n\n");
        Request request = Request.parseFromSocket(socketConnection);
        UnauthorisedResource resource = new UnauthorisedResource();

        assertEquals(Response.UNAUTHORISED_RESPONSE, new String(resource.generateResponse(request)));
    }
}
