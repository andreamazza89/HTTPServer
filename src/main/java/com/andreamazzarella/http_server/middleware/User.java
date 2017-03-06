package com.andreamazzarella.http_server.middleware;

public class User {

    private final String userName;
    private final String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    String getUserName() {
        return userName;
    }

    String getPassword() {
        return password;
    }
}
