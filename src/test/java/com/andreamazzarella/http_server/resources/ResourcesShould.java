package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.BasicAuthenticator;
import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;
import java.util.Base64;

import static junit.framework.TestCase.assertEquals;

public class ResourcesShould {

    @Test
    public void provideAMissingRouteIfNoneMatchesTheGivenURI() {
        Resources resources = new Resources(new BasicAuthenticator());

        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /i/am/not/a/real/path HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        Resource resource = resources.findResource(request);

        assertEquals(MissingResource.class, resource.getClass());
    }

    @Test
    public void provideTheResourceAssociatedToTheGivenURI() {
        Resources resources = new Resources(new BasicAuthenticator());
        Resource tea = new TeaPotResource(URI.create("/tea"));
        resources.addResource(tea);

        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /tea HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        Resource resource = resources.findResource(request);

        assertEquals(tea, resource);
    }

    @Test
    public void provideUnauthorisedResourceWhenCredentialsAreInvalid() {
        BasicAuthenticator authenticator = new BasicAuthenticator();
        authenticator.addUser("admin", "monkey_password");
        Resources resources = new Resources(authenticator);
        Resource tea = new TeaPotResource(URI.create("/tea"));
        resources.addResource(tea);
        resources.requireAuthenticationFor(tea);

        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("GET /tea HTTP/1.1\n\n");
        Request request = new Request(socketConnection);

        Resource resource = resources.findResource(request);

        assertEquals(UnauthorisedResource.class, resource.getClass());
    }

    @Test
    public void provideResourceWhenCredentialsAreValid() {
        BasicAuthenticator authenticator = new BasicAuthenticator();
        authenticator.addUser("admin", "monkey_password");
        Resources resources = new Resources(authenticator);
        Resource tea = new TeaPotResource(URI.create("/tea"));
        resources.addResource(tea);
        resources.requireAuthenticationFor(tea);

        FakeSocketConnection socketConnection = new FakeSocketConnection();
        String authCredentials = "admin:monkey_password";
        String encodedCredentials = new String(Base64.getEncoder().encode(authCredentials.getBytes()));
        socketConnection.setRequestTo("GET /tea HTTP/1.1\nAuthorization: Basic " + encodedCredentials + "\n\n");
        Request request = new Request(socketConnection);

        Resource resource = resources.findResource(request);

        assertEquals(tea, resource);
    }
}
