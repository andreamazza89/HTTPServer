package com.andreamazzarella.http_server;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeaderShould {

    @Test
    public void haveAStringRepresentation() {
        Header header = new Header("Name", "field");

        assertEquals("Name: field\n", header.toString());
    }
}
