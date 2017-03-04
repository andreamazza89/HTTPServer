package com.andreamazzarella.http_server.middleware;

import com.andreamazzarella.http_server.ArrayOperations;
import com.andreamazzarella.http_server.FileSystem;
import com.andreamazzarella.http_server.Response;
import com.andreamazzarella.http_server.request.Request;

import java.net.URI;
import java.util.List;

public class Logger implements MiddleWare {

    private final MiddleWare nextLayer;
    private final FileSystem fileSystem;
    private final URI logsPath;
    private final List<URI> routesToLog;

    public Logger(MiddleWare nextLayer, FileSystem loggingFileSystem, URI logsPath, List<URI> routesToLog) {
        this.nextLayer = nextLayer;
        this.fileSystem = loggingFileSystem;
        this.logsPath = logsPath;
        this.routesToLog = routesToLog;
    }

    @Override
    public Response generateResponseFor(Request request) {
        log(request);
        return nextLayer.generateResponseFor(request);
    }

    private void log(Request request) {
        if (routesToLog.contains(request.getUri())) {
            String requestLine = request.getRequestLine();
            byte[] requestLineWithNewLine = ArrayOperations.concatenateData(requestLine.getBytes(), "\n".getBytes());
            fileSystem.appendContent(logsPath, requestLineWithNewLine);
        }
    }
}
