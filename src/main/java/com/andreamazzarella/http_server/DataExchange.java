package com.andreamazzarella.http_server;

public interface DataExchange {
    String readLine();
    void write(byte[] data);
    void read(char[] buffer, int startIndex, int contentLenth);
}
