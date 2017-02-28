package com.andreamazzarella.http_server;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicAuthenticator {
    private final Map<String, String> users = new HashMap<>();

    public void addUser(String userName, String password) {
        users.put(userName, password);
    }

    public boolean isRequestValid(Request request) {
        String authorizationHeaderFieldContent = request.getHeader("Authorization");

        if (authorizationHeaderFieldContent == null) {
            return false;
        }

        Pattern encodedCredentialsPattern = Pattern.compile(".*Basic\\s(?<credentials>.+)");
        Matcher encodedCredentialsMatcher = encodedCredentialsPattern.matcher(authorizationHeaderFieldContent);
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
