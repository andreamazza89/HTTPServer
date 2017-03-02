package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.DataExchange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class FakeSocketConnection implements DataExchange {

    private static final int LINE_FEED = 10;
    private static final int CARRIAGE_RETURN = 13;

    private InputStream requestInputStream;







    @Override
    public String readLine() {
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            int nextCharacter = 0;
            try {
                nextCharacter = requestInputStream.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nextCharacter == LINE_FEED || nextCharacter == CARRIAGE_RETURN) {
                    break;
                } else {
                    stringBuilder.append((char)nextCharacter);
                }
        }

        return stringBuilder.toString();
    }

    @Override
    public void write(byte[] data) {
        throw new RuntimeException("method not implemented as not required yet in Fake Object");
    }

    @Override
    public void read(byte[] buffer, int startIndex, int contentLength) {
        try {
            requestInputStream.read(buffer, startIndex, contentLength);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void setRequestTo(String request) {
        this.requestInputStream = new ByteArrayInputStream(request.getBytes());
    }
}
