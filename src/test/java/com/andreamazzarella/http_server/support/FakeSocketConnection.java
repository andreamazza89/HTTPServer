package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.DataExchange;

import java.io.*;

public class FakeSocketConnection implements DataExchange {

    private final ByteArrayOutputStream messagesReceived;
    private BufferedReader request;

    public FakeSocketConnection() {
        this.messagesReceived = new ByteArrayOutputStream();
    }

    @Override
    public String readLine() {
        try {
            return request.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void write(String data) {
        try {
            messagesReceived.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void read(char[] buffer, int startIndex, int contentLenth) {
        try {
            request.read(buffer, startIndex, contentLenth);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    public String messageReceived() {
        return messagesReceived.toString();
    }

    public void setRequestTo(String request) {
        this.request = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
    }
}
