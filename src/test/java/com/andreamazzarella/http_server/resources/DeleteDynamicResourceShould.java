package com.andreamazzarella.http_server.resources;

import com.andreamazzarella.http_server.Request;
import com.andreamazzarella.http_server.support.FakeFileSystem;
import com.andreamazzarella.http_server.support.FakeSocketConnection;
import org.junit.Test;

import java.net.URI;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class DeleteDynamicResourceShould {

    @Test
    public void deleteContent() {
        URI resourcePath = URI.create("/path_to_dynamic_resource/");
        String resourceContent = "Something has to disappear";
        FakeFileSystem fileSystem = new FakeFileSystem(URI.create("./fake_directory"));
        fileSystem.addOrReplaceResource(resourcePath, resourceContent.getBytes());
        FakeSocketConnection socketConnection = new FakeSocketConnection();
        socketConnection.setRequestTo("DELETE " + resourcePath + " HTTP/1.1\n\n");
        Request request = new Request(socketConnection);
        Resource deleteDynamicResource = new DeleteDynamicResource(resourcePath, fileSystem);

        assertEquals(Response.STATUS_TWO_HUNDRED + Response.END_OF_HEADERS, new String(deleteDynamicResource.generateResponse(request)));
        assertEquals(Optional.empty(), fileSystem.getResource(resourcePath, null));
    }
}
