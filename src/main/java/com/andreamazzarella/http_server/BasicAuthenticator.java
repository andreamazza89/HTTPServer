package com.andreamazzarella.http_server;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BasicAuthenticator {
    private final Map<String, String> users = new HashMap<>();

    void addUser(String userName, String password) {
        users.put(userName, password);
    }

    boolean isRequestValid(Request request) {
        String authorizationHeaderFieldContent = request.getHeader("Authorization");

        if (authorizationHeaderFieldContent == null) {
            return false;
        }

        Pattern encodedCredentialsPattern = Pattern.compile(".*Basic\\s(.+)");
        Matcher encodedCredentialsMatcher = encodedCredentialsPattern.matcher(authorizationHeaderFieldContent);
        encodedCredentialsMatcher.matches();
        String encodedUserCredentials = encodedCredentialsMatcher.group(1);
        String userCredentials = new String(Base64.getDecoder().decode(encodedUserCredentials));

        Pattern decodedCredentialsPattern = Pattern.compile("(.+):(.+)");
        Matcher decodedCredentialsMatcher = decodedCredentialsPattern.matcher(userCredentials);
        decodedCredentialsMatcher.matches();

        String requestUserName = decodedCredentialsMatcher.group(1);
        String requestPassword = decodedCredentialsMatcher.group(2);

        return isUserRegistered(requestUserName) && (isPasswordValid(requestUserName, requestPassword));

    }

    private boolean isUserRegistered(String userName) {
        return users.containsKey(userName);
    }

    private boolean isPasswordValid(String userName, String password) {
        return users.get(userName).equals(password);
    }
}
