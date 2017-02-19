package com.andreamazzarella.http_server;

import java.io.*;
import java.net.Socket;

public class SocketConnection implements DataExchange {

    private BufferedReader reader;
    private PrintStream writer;

    SocketConnection(Socket socket) {
        try {
            this.writer = new PrintStream(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void write(String data) {
        writer.println(data);
    }
}
