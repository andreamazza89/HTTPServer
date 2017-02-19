package com.andreamazzarella.http_server;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InputParserShould {

    private static final int DEFAULT_PORT_NUMBER = 5000;

    @Test
    public void parseAPortNumber() {
        String[] args = {"-p", "3030", "-d", "ciao/ciao/miao"};
        InputParser inputParser = new InputParser(args);
        assertEquals(3030, inputParser.parsePortNumber());
    }

    @Test
    public void parseAnotherPortNumber() {
        String[] args = {"-p", "3031", "-d", "ciao/ciao/miao"};
        InputParser inputParser = new InputParser(args);
        assertEquals(3031, inputParser.parsePortNumber());
    }

    @Test
    public void parsePortNumberBasedOnTheFlag() {
        String[] args = {"-d", "ciao/ciao/miao", "-p", "3031"};
        InputParser inputParser = new InputParser(args);
        assertEquals(3031, inputParser.parsePortNumber());
    }

    @Test
    public void setDefaultPortNumberIfNotProvided() {
        String[] args = {"-d", "ciao/ciao/miao"};
        InputParser inputParser = new InputParser(args);
        assertEquals(DEFAULT_PORT_NUMBER, inputParser.parsePortNumber());
    }

    @Test
    public void parseADirectoryPath() {
        String[] args = {"-p", "3030", "-d", "ciao/ciao/miao"};
        InputParser inputParser = new InputParser(args);
        assertEquals("ciao/ciao/miao", inputParser.parseDirectoryPath());
    }

    @Test
    public void parseAnotherDirectoryPath() {
        String[] args = {"-p", "3030", "-d", "lol/ciao/miao"};
        InputParser inputParser = new InputParser(args);
        assertEquals("lol/ciao/miao", inputParser.parseDirectoryPath());
    }

    @Test
    public void parseADirectoryPathBasedOnFlag() {
        String[] args = {"-d", "ciao/gigi/come/va", "-p", "3031"};
        InputParser inputParser = new InputParser(args);
        assertEquals("ciao/gigi/come/va", inputParser.parseDirectoryPath());
    }
}
