package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.Header;
import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.support.FakeMiddleWare;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.andreamazzarella.http_server.Response.StatusCode._200;
import static com.andreamazzarella.http_server.Response.StatusCode._401;
import static com.andreamazzarella.http_server.Response.StatusCode._418;
import static org.junit.Assert.assertEquals;

public class BasicAuthenticatorShould {

    @Test
    public void generateANotAuthorisedResponseIfAuthenticationHeaderIsMissing() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer);
        basicAuthenticator.requireAuthenticationFor(URI.create("/i_am_not_so_authentic"));
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", new ArrayList<>(), Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(_401, response.getStatusCode());
    }

    @Test
    public void respondWithWhateverResponseIsProvidedByTheNextMiddlewareLayerIfRouteDoesNotRequireAuthenticationExampleOne() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        Response expectedResponse = new Response(_418);
        nextLayer.stubResponse(expectedResponse);
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer);
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", new ArrayList<>(), Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void respondWithWhateverResponseIsProvidedByTheNextMiddlewareLayerIfRouteDoesNotRequireAuthenticationExampleTwo() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        Response expectedResponse = new Response(_200);
        nextLayer.stubResponse(expectedResponse);
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer);
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", new ArrayList<>(), Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void respondWithWhateverResponseIsProvidedByTheNextMiddlewareLayerIfCredentialsAreValid() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        Response expectedResponse = new Response(_418);
        nextLayer.stubResponse(expectedResponse);
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer);
        basicAuthenticator.requireAuthenticationFor(URI.create("/i_am_not_so_authentic"));
        basicAuthenticator.addUser("admin", "monkey_password");
        String authCredentials = "admin:monkey_password";
        String encodedCredentials = new String(Base64.getEncoder().encode(authCredentials.getBytes()));
        List<Header> headers = new ArrayList<>();
        headers.add(new Header(Header.AUTHORIZATION_HEADER_NAME, "Basic: " + encodedCredentials));
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", headers, Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void generateANotAuthorisedResponseIfAuthenticationFails() {
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(nextLayer);
        basicAuthenticator.requireAuthenticationFor(URI.create("/i_am_not_so_authentic"));
        basicAuthenticator.addUser("admin", "monkey_password");
        String authCredentials = "admin:wrong_password";
        String encodedCredentials = new String(Base64.getEncoder().encode(authCredentials.getBytes()));
        List<Header> headers = new ArrayList<>();
        headers.add(new Header(Header.AUTHORIZATION_HEADER_NAME, "Basic: " + encodedCredentials));
        Request request = new Request("GET /i_am_not_so_authentic HTTP/1.1", headers, Optional.empty());

        Response response = basicAuthenticator.generateResponseFor(request);

        assertEquals(_401, response.getStatusCode());
    }
}
