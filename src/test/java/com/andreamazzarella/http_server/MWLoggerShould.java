package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.middleware.MWLogger;
import com.andreamazzarella.http_server.middleware.MiddleWare;
import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeMiddleWare;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class MWLoggerShould {

    @Test
    public void respondWithWhateverResponseIsProvidedByTheNextMiddlewareLayer() {
        Request request = new Request("GET /log/me HTTP/1.1", new ArrayList<>(), Optional.empty());
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        MWResponse expectedResponse = new MWResponse(200);
        nextLayer.stubResponse(expectedResponse);
        MiddleWare mwLogger = new MWLogger(nextLayer, new FakeFileSystem(URI.create("./logs")));

        MWResponse actualResponse = mwLogger.generateResponseFor(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void logRequestsItIsFollowingToTheFileSystemWriterProvided() {
        URI loggingPath = URI.create("./logs");
        FakeFileSystem fileSystem = new FakeFileSystem(loggingPath);

        URI resourcePath = URI.create("/looking_at_you");
        Request request = new Request("LOG_THIS " + resourcePath + " HTTP/1.1", new ArrayList<>(), Optional.empty());

        URI otherResourcePath = URI.create("/log_me");
        Request otherRequest = new Request("AND_THIS " + otherResourcePath + " HTTP/1.1", new ArrayList<>(), Optional.empty());

        MWLogger logger = new MWLogger(new FakeMiddleWare(), fileSystem);
        logger.follow(resourcePath);
        logger.follow(otherResourcePath);

        logger.log(request);
        logger.log(otherRequest);

        String expectedLog = "LOG_THIS " + resourcePath + " HTTP/1.1\n" + "AND_THIS " + otherResourcePath + " HTTP/1.1\n";
        assertEquals(expectedLog, new String(fileSystem.getResource(loggingPath, null).get()));
    }

    @Test
    public void notLogARequestItIsNotFollowing() {
        URI loggingPath = URI.create("./logs");
        FakeFileSystem fileSystem = new FakeFileSystem(loggingPath);

        URI resourcePath = URI.create("/looking_at_you");
        Request request = new Request("LOG_THIS " + resourcePath + " HTTP/1.1", new ArrayList<>(), Optional.empty());
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        MWResponse expectedResponse = new MWResponse(200);
        nextLayer.stubResponse(expectedResponse);

        MWLogger logger = new MWLogger(nextLayer, fileSystem);

        logger.log(request);

        assertEquals(Optional.empty(), fileSystem.getResource(loggingPath, null));
    }
}
