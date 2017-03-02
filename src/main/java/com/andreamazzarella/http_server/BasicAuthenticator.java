package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.headers.Header;
import com.andreamazzarella.http_server.request.Request;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicAuthenticator {
    private final Map<String, String> users = new HashMap<>();

    public void addUser(String userName, String password) {
        users.put(userName, password);
    }

    public boolean isRequestValid(Request request) {

        Optional<Header> optionalAuthorizationHeader = request.getAuthorizationHeader();

        if (!optionalAuthorizationHeader.isPresent()) {
            return false;
        }

        Header authorizationHeader = optionalAuthorizationHeader.get();

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
        return users.containsKey(userName);
    }

    private boolean isPasswordValid(String userName, String password) {
        return users.get(userName).equals(password);
    }
}
