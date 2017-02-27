package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class BasicAuthenticatorShould {

    @Test
    public void notAuthenticateARequestMissingAuthenticationHeader() {
        BasicAuthenticator authenticator = new BasicAuthenticator();
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("POST /tea HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        assertEquals(false, authenticator.isRequestAuthenticated(request));
    }

    @Test
    public void notAuthenticateARequestWithBadCredentials() {
        BasicAuthenticator authenticator = new BasicAuthenticator();
        authenticator.addUser("admin", "monkey_password");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        String authCredentials = "admin:wrong_password";
        String encodedCredentials = new String(Base64.getEncoder().encode(authCredentials.getBytes()));
        socketConnection.setRequestTo("POST /tea HTTP/1.1\nAuthorization: Basic " + encodedCredentials + "\n\n");
        Request request = new Request(socketConnection);

        assertEquals(false, authenticator.isRequestAuthenticated(request));
    }

    @Test
    public void authenticateARequestWithGoodCredentials() {
        BasicAuthenticator authenticator = new BasicAuthenticator();
        authenticator.addUser("admin", "monkey_password");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        String authCredentials = "admin:monkey_password";
        String encodedCredentials = new String(Base64.getEncoder().encode(authCredentials.getBytes()));
        socketConnection.setRequestTo("POST /tea HTTP/1.1\nAuthorization: Basic " + encodedCredentials + "\n\n");
        Request request = new Request(socketConnection);

        assertEquals(true, authenticator.isRequestAuthenticated(request));
    }
}
