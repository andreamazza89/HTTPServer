package com.andreamazzarella.http_server.middleware.controllers;

import com.andreamazzarella.http_server.utilities.DataRange;
import com.andreamazzarella.http_server.utilities.FileSystem;
import com.andreamazzarella.http_server.request_response.Response;
import com.andreamazzarella.http_server.request_response.Request;

import java.net.URI;

import static com.andreamazzarella.http_server.request_response.Response.StatusCode._200;

public class LogsController extends BaseController {

    private final FileSystem loggingFileSystem;
    private final URI logsFileName;

    public LogsController(FileSystem loggingFileSystem, URI logsFileName) {
        this.loggingFileSystem = loggingFileSystem;
        this.logsFileName = logsFileName;
    }

    protected Response get(Request request) {
        byte[] logs = loggingFileSystem.getResource(logsFileName, new DataRange());
        return new Response(_200).setBody(logs);
    }
}
