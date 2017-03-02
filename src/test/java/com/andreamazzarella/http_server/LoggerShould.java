package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class LoggerShould {

    @Test
    public void logARequestItIsFollowingToTheURIProvided() {
        URI loggingPath = URI.create("./logs");
        FakeFileSystem fileSystem = new FakeFileSystem(loggingPath);
        URI resourcePath = URI.create("/looking_at_you");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("LOG_THIS " + resourcePath + " HTTP/1.1\n\n");
        Request request = Request.parseFromSocket(socketConnection);
        Logger logger = new Logger(fileSystem);
        logger.follow(resourcePath);

        logger.log(request);

        assertEquals("LOG_THIS " + resourcePath + " HTTP/1.1\n", new String(fileSystem.getResource(loggingPath, null).get()));
    }

    @Test
    public void notLogARequestItIsNotFollowing() {
        URI loggingPath = URI.create("./logs");
        FakeFileSystem fileSystem = new FakeFileSystem(loggingPath);
        URI resourcePath = URI.create("/dont_care_what_happens_here");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("LOG_THIS " + resourcePath + " HTTP/1.1\n\n");
        Request request = Request.parseFromSocket(socketConnection);
        Logger logger = new Logger(fileSystem);

        logger.log(request);

        assertEquals(Optional.empty(), fileSystem.getResource(loggingPath, null));
    }

    @Test
    public void logMultipleRequestsItIsFollowing() {
        URI loggingPath = URI.create("./logs");
        FakeFileSystem fileSystem = new FakeFileSystem(loggingPath);

        URI resourcePathOne = URI.create("/looking_at_you");
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("LOG_THIS " + resourcePathOne + " HTTP/1.1\n\n");
        Request requestOne = Request.parseFromSocket(socketConnection);

        URI resourcePathTwo = URI.create("/log_me");
        FakeSocketConnection socketConnectionTwo = new FakeSocketConnection();
        socketConnectionTwo.setRequestTo("AND_THIS " + resourcePathTwo + " HTTP/1.1\n\n");
        Request requestTwo = Request.parseFromSocket(socketConnectionTwo);

        Logger logger = new Logger(fileSystem);
        logger.follow(resourcePathOne);
        logger.follow(resourcePathTwo);

        logger.log(requestOne);
        logger.log(requestTwo);

        String expectedLog = "LOG_THIS " + resourcePathOne + " HTTP/1.1\n" + "AND_THIS " + resourcePathTwo + " HTTP/1.1\n";
        assertEquals(expectedLog, new String(fileSystem.getResource(loggingPath, null).get()));
    }
}
