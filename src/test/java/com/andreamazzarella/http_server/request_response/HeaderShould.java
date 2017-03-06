package com.andreamazzarella.http_server.request_response;

import com.andreamazzarella.http_server.request_response.Header;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeaderShould {

    @Test
    public void haveAStringRepresentation() {
        Header header = new Header("Name", "field");

        assertEquals("Name: field\n", header.toString());
    }
}
