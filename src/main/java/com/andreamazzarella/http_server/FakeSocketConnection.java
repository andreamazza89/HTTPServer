package com.andreamazzarella.http_server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FakeSocketConnection implements DataExchange {

    private final ByteArrayOutputStream messagesReceived;
    private String request;

    FakeSocketConnection() {
        this.messagesReceived = new ByteArrayOutputStream();
    }

    @Override
    public String readLine() {
        return request;
    }

    @Override
    public void write(String data) {
        try {
            messagesReceived.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String messageReceived() {
        return messagesReceived.toString();
    }

    public void setRequestTo(String request) {
        this.request = request;
    }
}
