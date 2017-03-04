package com.andreamazzarella.http_server;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.assertEquals;

public class FileSystemShould {

    @Test
    public void knowIfAResourceDoesNotExist() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));

        assertEquals(true, fileSystem.resourceDoesNotExist(URI.create("/vain_attempt_to_retrieve_resource")));
    }

    @Test
    public void knowIfAResourceExists() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));
        URI pathToResource = URI.create("/pretty_sure_this_one_exists");
        fileSystem.addOrReplaceResource(pathToResource, "irrelevant content".getBytes());

        assertEquals(false, fileSystem.resourceDoesNotExist(pathToResource));
    }

    @Test
    public void retrieveAnExistingResource() throws IOException {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));
        URI pathToResource = URI.create("/my_resource");

        File resource = new File("./resources" + pathToResource);
        Files.write(resource.toPath(), "I am such a resourceful resource!".getBytes(), StandardOpenOption.CREATE);

        assertEquals("I am such a resourceful resource!", new String(fileSystem.getResource(pathToResource, null)));
    }

    @Test
    public void retrievePartOfAnExistingResourceFullRange() throws IOException {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));
        URI pathToResource = URI.create("/my_partial_resource");

        File resource = new File("./resources" + pathToResource);
        Files.write(resource.toPath(), "Please enjoy this resource in chunks...".getBytes(), StandardOpenOption.CREATE);

        assertEquals("Please ", new String(fileSystem.getResource(pathToResource, "bytes=0-6")));
    }

    @Test
    public void retrievePartOfAnExistingResourceMissingStart() throws IOException {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));
        URI pathToResource = URI.create("/my_partial_resource");

        File resource = new File("./resources" + pathToResource);
        Files.write(resource.toPath(), "Please enjoy this resource in chunks...".getBytes(), StandardOpenOption.CREATE);

        assertEquals("chunks...", new String(fileSystem.getResource(pathToResource, "bytes=-9")));
    }

    @Test
    public void retrievePartOfAnExistingResourceMissingEnd() throws IOException {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));
        URI pathToResource = URI.create("/my_partial_resource");

        File resource = new File("./resources" + pathToResource);
        Files.write(resource.toPath(), "Please enjoy this resource in chunks...".getBytes(), StandardOpenOption.CREATE);

        assertEquals("enjoy this resource in chunks...", new String(fileSystem.getResource(pathToResource, "bytes=7-")));
    }

    @Test
    public void addANewResource() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));

        URI pathToResource = URI.create("/my_brand_new_resource");
        fileSystem.addOrReplaceResource(pathToResource, "what a handsome resource".getBytes());

        assertEquals("what a handsome resource", new String(fileSystem.getResource(pathToResource, null)));
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

        assertEquals("not so fast Giacomo", new String(fileSystem.getResource(pathToResource, null)));
    }

    @Test
    public void appendNewContentToAnExistingResource() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));

        URI pathToResource = URI.create("/my_brand_new_resource");
        fileSystem.addOrReplaceResource(pathToResource, "what a handsome resource".getBytes());
        fileSystem.appendContent(pathToResource, "not so fast Giacomo".getBytes());

        assertEquals("what a handsome resourcenot so fast Giacomo", new String(fileSystem.getResource(pathToResource, null)));
    }

    @Test
    public void createANewResourceWhenAppendingToInexistingResource() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));

        URI pathToResource = URI.create("/my_brand_new_resource");
        fileSystem.appendContent(pathToResource, "not so fast Giacomo".getBytes());

        assertEquals("not so fast Giacomo", new String(fileSystem.getResource(pathToResource, null)));
    }

    @Test
    public void deleteAnExistingResource() {
        FileSystem fileSystem = new FileSystem(URI.create("./resources/"));

        URI pathToResource = URI.create("/my_brand_new_resource");
        fileSystem.addOrReplaceResource(pathToResource, "what a handsome resource".getBytes());

        fileSystem.deleteResource(pathToResource);

        assertEquals(true, fileSystem.resourceDoesNotExist(pathToResource));
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
