package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Header;
import com.andreamazzarella.http_server.request_response.Request;
import com.andreamazzarella.http_server.support.FakeMiddleWare;
import org.junit.Test;

import java.net.URI;
import java.util.*;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode._200;
import static com.andreamazzarella.http_server.request_response.Response.StatusCode._401;
import static com.andreamazzarella.http_server.request_response.Response.StatusCode._418;
import static org.junit.Assert.assertEquals;

public class BasicAuthenticatorShould {

    @Test
    public void generateANotAuthorisedResponseIfAuthenticationHeaderIsMissing() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        List<URI> routesToAuthenticate = new ArrayList<>(Arrays.asList(URI.create("/i_am_not_so_authentic")));
        List<User> users = new ArrayList<>(Arrays.asList(new User("admin", "monkey_password")));
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer, users, routesToAuthenticate);
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", new ArrayList<>(), Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(_401, response.getStatusCode());
        assertEquals("WWW-Authenticate", response.getHeaders().get(0).getName());
        assertEquals("Basic", response.getHeaders().get(0).getValue());
    }

    @Test
    public void respondWithWhateverResponseIsProvidedByTheNextMiddlewareLayerIfRouteDoesNotRequireAuthenticationExampleOne() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        Response expectedResponse = new Response(_418);
        nextLayer.stubResponse(expectedResponse);
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer, new ArrayList<>(), new ArrayList<>());
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", new ArrayList<>(), Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void respondWithWhateverResponseIsProvidedByTheNextMiddlewareLayerIfRouteDoesNotRequireAuthenticationExampleTwo() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        Response expectedResponse = new Response(_200);
        nextLayer.stubResponse(expectedResponse);
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer, new ArrayList<>(), new ArrayList<>());
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", new ArrayList<>(), Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void respondWithWhateverResponseIsProvidedByTheNextMiddlewareLayerIfCredentialsAreValid() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        Response expectedResponse = new Response(_418);
        nextLayer.stubResponse(expectedResponse);
        List<User> users = new ArrayList<>(Arrays.asList(new User("admin", "monkey_password")));
        List<URI> routesToAuthenticate = new ArrayList<>(Arrays.asList(URI.create("/i_am_not_so_authentic")));
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer, users, routesToAuthenticate);
        String authCredentials = "admin:monkey_password";
        String encodedCredentials = new String(Base64.getEncoder().encode(authCredentials.getBytes()));
        List<Header> headers = new ArrayList<>();
        headers.add(new Header(Header.AUTHORIZATION_HEADER_NAME, "Basic " + encodedCredentials));
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", headers, Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void generateANotAuthorisedResponseIfAuthenticationFails() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        List<User> users = new ArrayList<>(Arrays.asList(new User("admin", "monkey_password")));
        List<URI> routesToAuthenticate = new ArrayList<>(Arrays.asList(URI.create("/i_am_not_so_authentic")));
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer, users, routesToAuthenticate);
        String authCredentials = "admin:wrong_password";
        String encodedCredentials = new String(Base64.getEncoder().encode(authCredentials.getBytes()));
        List<Header> headers = new ArrayList<>();
        headers.add(new Header(Header.AUTHORIZATION_HEADER_NAME, "Basic " + encodedCredentials));
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", headers, Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(_401, response.getStatusCode());
        assertEquals("WWW-Authenticate", response.getHeaders().get(0).getName());
        assertEquals("Basic", response.getHeaders().get(0).getValue());
    }
}
