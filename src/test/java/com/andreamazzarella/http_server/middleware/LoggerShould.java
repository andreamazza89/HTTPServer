package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeMiddleWare;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.andreamazzarella.http_server.Response.StatusCode._200;
import static org.junit.Assert.assertEquals;

public class LoggerShould {

    @Test
    public void respondWithWhateverResponseIsProvidedByTheNextMiddlewareLayer() {
        Request request = new Request("GET /log/me HTTP/1.1", new ArrayList<>(), Optional.empty());
        FakeMiddleWare nextLayer = new FakeMiddleWare();
        Response expectedResponse = new Response(_200);
        nextLayer.stubResponse(expectedResponse);
        URI logsPath = URI.create("/logs");
        List<URI> routesToLog = new ArrayList<>();
        MiddleWare mwLogger = new Logger(nextLayer, new FakeFileSystem(URI.create("./logs")), logsPath, routesToLog);

        Response actualResponse = mwLogger.generateResponseFor(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void logRequestsItIsFollowingToTheFileSystemWriterProvided() {
        URI loggingDirectory = URI.create("./logs");
        FakeFileSystem fileSystem = new FakeFileSystem(loggingDirectory);

        URI resourcePath = URI.create("/looking_at_you");
        Request request = new Request("LOG_THIS " + resourcePath + " HTTP/1.1", new ArrayList<>(), Optional.empty());

        URI otherResourcePath = URI.create("/log_me");
        Request otherRequest = new Request("AND_THIS " + otherResourcePath + " HTTP/1.1", new ArrayList<>(), Optional.empty());

        URI logsPath = URI.create("/logs");
        List<URI> routesToLog = new ArrayList<>(Arrays.asList(resourcePath, otherResourcePath));;
        Logger logger = new Logger(new FakeMiddleWare(), fileSystem, logsPath, routesToLog);

        logger.generateResponseFor(request);
        logger.generateResponseFor(otherRequest);

        String expectedLog = "LOG_THIS " + resourcePath + " HTTP/1.1\n" + "AND_THIS " + otherResourcePath + " HTTP/1.1\n";
        assertEquals(expectedLog, new String(fileSystem.getResource(logsPath, null)));
    }

    @Test
    public void notLogARequestItIsNotFollowing() {
        URI loggingDirectory = URI.create("./logs");
        FakeFileSystem fileSystem = new FakeFileSystem(loggingDirectory);

        URI resourcePath = URI.create("/looking_at_you");
        Request request = new Request("LOG_THIS " + resourcePath + " HTTP/1.1", new ArrayList<>(), Optional.empty());

        URI logsPath = URI.create("/logs");
        List<URI> routesToLog = new ArrayList<>();
        Logger logger = new Logger(new FakeMiddleWare(), fileSystem, logsPath, routesToLog);

        logger.generateResponseFor(request);

        assertEquals(true, fileSystem.resourceDoesNotExist(logsPath));
    }
}
