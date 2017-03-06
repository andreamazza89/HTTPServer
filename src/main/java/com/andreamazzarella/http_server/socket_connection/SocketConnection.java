package com.andreamazzarella.http_server.socket_connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.Socket;

public class SocketConnection implements DataExchange {

    private static final int LINE_FEED = 10;
    private static final int CARRIAGE_RETURN = 13;

    private final InputStream inputStream;
    private PrintStream writer;

    public SocketConnection(Socket socket) {
        try {
            this.writer = new PrintStream(socket.getOutputStream(), true, "UTF-8");
            this.inputStream = socket.getInputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String readLine() {
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            int nextCharacter;
            try {
                nextCharacter = inputStream.read();
                if (nextCharacter == LINE_FEED || nextCharacter == CARRIAGE_RETURN) {
                    inputStream.read(); //this relies on the fact that the request_response send has two newline characters; would be good not to make this assumption
                    break;
                } else {
                    stringBuilder.append((char)nextCharacter);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public void read(byte[] buffer, int startIndex, int contentLength) {
        try {
            inputStream.read(buffer, startIndex, contentLength);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void write(byte[] data) {
        try {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
