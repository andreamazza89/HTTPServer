package com.andreamazzarella.http_server;

public interface DataExchange {
    String readLine();
    void write(byte[] data);
    void read(byte[] buffer, int startIndex, int contentLength);
}
