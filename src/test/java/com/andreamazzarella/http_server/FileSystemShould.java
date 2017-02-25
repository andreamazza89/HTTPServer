package com.andreamazzarella.http_server;

import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class FileSystemShould {

    @Test
    public void supportMissingResources() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));

        assertEquals(Optional.empty(), fileSystem.getResource(URI.create("/inexistent/resource")));
    }

    @Test
    public void supportEmptyResources() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));
        URI pathToResource = URI.create("/my_brand_new_empty_resource");
        fileSystem.addOrReplaceResource(pathToResource, "".getBytes());

        assertEquals(Optional.empty(), fileSystem.getResource(pathToResource));
    }

    @Test
    public void retrieveAnExistingResource() throws IOException {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));
        URI pathToResource = URI.create("/my_resource");

        File resource = new File("./resources" + pathToResource);
        String path = resource.getCanonicalPath();
        PrintWriter printWriter = new PrintWriter(path);
        printWriter.print("I am such a resourceful resource!");
        printWriter.close();

        assertArrayEquals("I am such a resourceful resource!".getBytes(), fileSystem.getResource(pathToResource).get());
    }

    @Test
    public void addANewResource() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));

        URI pathToResource = URI.create("/my_brand_new_resource");
        fileSystem.addOrReplaceResource(pathToResource, "what a handsome resource".getBytes());

        assertArrayEquals("what a handsome resource".getBytes(), fileSystem.getResource(pathToResource).get());
    }

    @Test
    public void provideTheResourceContentTypeExampleJPEG() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));
        URI pathToResource = URI.create("/my_resource.jpeg");
        fileSystem.addOrReplaceResource(pathToResource, "ciao I am a pretty picture".getBytes());

        assertEquals("image/jpeg", fileSystem.getResourceContentType(pathToResource));
    }

    @Test
    public void provideTheResourceContentTypeExampleGif() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));
        URI pathToResource = URI.create("/my_resource.gif");
        fileSystem.addOrReplaceResource(pathToResource, "ciao I am a pretty picture".getBytes());

        assertEquals("image/gif", fileSystem.getResourceContentType(pathToResource));
    }

    @Test
    public void updateAnExistingResource() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));

        URI pathToResource = URI.create("/my_brand_new_resource");
        fileSystem.addOrReplaceResource(pathToResource, "what a handsome resource".getBytes());
        fileSystem.addOrReplaceResource(pathToResource, "not so fast Giacomo".getBytes());

        assertArrayEquals("not so fast Giacomo".getBytes(), fileSystem.getResource(pathToResource).get());

    }

    @Test
    public void deleteAnExistingResource() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));

        URI pathToResource = URI.create("/my_brand_new_resource");
        fileSystem.addOrReplaceResource(pathToResource, "what a handsome resource".getBytes());

        fileSystem.deleteResource(pathToResource);

        assertEquals(Optional.empty(), fileSystem.getResource(pathToResource));
    }

    @After
    public void tearDown() {
        File resource = new File("./resources");
        String[] files = resource.list();
        for(String file: files){
            File currentFile = new File(resource.getPath(), file);
            currentFile.delete();
        }
    }
}
