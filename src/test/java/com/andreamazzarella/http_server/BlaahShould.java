package com.andreamazzarella.http_server;

import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class BlaahShould {

    @Test
    public void supportMissingResources() {
        Blaah blaah = new Blaah(URI.create("/resources"));

        assertEquals(Optional.empty(), blaah.getResource(URI.create("/inexistent/resource")));
    }

    @Test
    public void retrieveAnExistingResource() throws IOException {
        Blaah blaah = new Blaah(URI.create("./resources/"));

        File resource = new File("./resources/my_resource");
        String path = resource.getCanonicalPath();
        PrintWriter printWriter = new PrintWriter(path);
        printWriter.print("I am such a resourceful resource!");
        printWriter.close();

        assertEquals(Optional.of("I am such a resourceful resource!"), blaah.getResource(URI.create("/my_resource")));
    }

    @Test
    public void addANewResource() {
        Blaah blaah = new Blaah(URI.create("./resources/"));

        URI pathToResource = URI.create("/my_brand_new_resource");
        blaah.addResource(pathToResource, "what a handsome resource");

        assertEquals(Optional.of("what a handsome resource"), blaah.getResource(pathToResource));
    }

    @Test
    public void updateAnExistingResource() {
        Blaah blaah = new Blaah(URI.create("./resources/"));

        URI pathToResource = URI.create("/my_brand_new_resource");
        blaah.addResource(pathToResource, "what a handsome resource");
        blaah.addResource(pathToResource, "not so fast Giacomo");

        assertEquals(Optional.of("not so fast Giacomo"), blaah.getResource(pathToResource));

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
