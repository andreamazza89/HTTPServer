package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.MWResponse;
import com.andreamazzarella.http_server.headers.Header;
import com.andreamazzarella.http_server.request.Request;

import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MWBasicAuthenticator implements MiddleWare {

    private final Map<String, String> users = new HashMap<>();
    private List<URI> routesRequiringAuthentication = new ArrayList<>();
    private final MiddleWare nextLayer;

    MWBasicAuthenticator(MiddleWare nextLayer) {
        this.nextLayer = nextLayer;
    }

    void requireAuthenticationFor(URI uri) {
        routesRequiringAuthentication.add(uri);
    }

    @Override
    public MWResponse generateResponseFor(Request request) {
        Optional<Header> authorizationHeader = request.getAuthorizationHeader();

        if (routeDoesNotNeedAuthentication(request)) {
            return new MWResponse(400);
        }

        if (requestIncludesAuthorizationHeader(authorizationHeader)) {
            return new MWResponse(401);
        }

        if (requestCredentialsAreValid(authorizationHeader.get())) {
            return nextLayer.generateResponseFor(request);
        } else {
            return new MWResponse(401);
        }

    }

    private boolean requestIncludesAuthorizationHeader(Optional<Header> optionalAuthorizationHeader) {
        return !optionalAuthorizationHeader.isPresent();
    }

    private boolean routeDoesNotNeedAuthentication(Request request) {
        return !routesRequiringAuthentication.contains(request.getUri());
    }

    void addUser(String userName, String password) {
        users.put(userName, password);
    }

    private boolean requestCredentialsAreValid(Header authorizationHeader) {
        Pattern encodedCredentialsPattern = Pattern.compile(".*Basic:\\s(?<credentials>.+)");
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
        return users.containsKey(userName);
    }

    private boolean isPasswordValid(String userName, String password) {
        return users.get(userName).equals(password);
    }
}
