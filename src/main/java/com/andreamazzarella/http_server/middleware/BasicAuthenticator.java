package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Header;
import com.andreamazzarella.http_server.request_response.Request;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode._401;

public class BasicAuthenticator implements MiddleWare {

    private List<User> users = new ArrayList<>();
    private List<URI> routesToAuthenticate = new ArrayList<>();
    private final MiddleWare nextLayer;

    public BasicAuthenticator(MiddleWare nextLayer, List<User> users, List<URI> routesToAuthenticate) {
        this.nextLayer = nextLayer;
        this.users = users;
        this.routesToAuthenticate = routesToAuthenticate;
    }

    @Override
    public Response generateResponseFor(Request request) {
        Optional<Header> authorizationHeader = request.getAuthorizationHeader();

        if (routeDoesNotNeedAuthentication(request) || credentialsAreValid(authorizationHeader)) {
            return nextLayer.generateResponseFor(request);
        } else {
            return new Response(_401).addHeader(new Header("WWW-Authenticate", "Basic"));
        }
    }

    private boolean routeDoesNotNeedAuthentication(Request request) {
        return !routesToAuthenticate.contains(request.getUri());
    }

    private boolean credentialsAreValid(Optional<Header> authorizationHeader) {
        return authorizationHeader.isPresent() && requestCredentialsAreValid(authorizationHeader.get());
    }

    private boolean requestCredentialsAreValid(Header authorizationHeader) {
        Pattern encodedCredentialsPattern = Pattern.compile(".*Basic\\s(?<credentials>.+)");
        Matcher encodedCredentialsMatcher = encodedCredentialsPattern.matcher(authorizationHeader.getValue());
        encodedCredentialsMatcher.matches();

        String encodedCredentials = encodedCredentialsMatcher.group("credentials");
        String credentials = new String(Base64.getDecoder().decode(encodedCredentials));


        Pattern decodedCredentialsPattern = Pattern.compile("(?<userName>.+):(?<password>.+)");
        Matcher decodedCredentialsMatcher = decodedCredentialsPattern.matcher(credentials);
        decodedCredentialsMatcher.matches();


        String requestUserName = decodedCredentialsMatcher.group("userName");
        String requestPassword = decodedCredentialsMatcher.group("password");

        return isUserRegistered(requestUserName) && (isPasswordValid(requestUserName, requestPassword));

    }

    private boolean isUserRegistered(String userName) {
        return users.stream().anyMatch((user) -> user.getUserName().equals(userName));
    }

    private boolean isPasswordValid(String userName, String password) {
        Optional<User> userFound = users.stream().filter((user) -> user.getUserName().equals(userName)).findFirst();
        return userFound.get().getPassword().equals(password);
    }
}
