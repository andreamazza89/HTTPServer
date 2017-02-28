package com.andreamazzarella.http_server.support;

import com.andreamazzarella.http_server.DataExchange;
import com.andreamazzarella.http_server.Request;

public class FakeRequest extends Request {

    public FakeRequest(DataExchange socketConnection) {
        super(socketConnection);
    }
}
