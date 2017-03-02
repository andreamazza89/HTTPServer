package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.ArrayOperations;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ArrayOperationsShould {

    @Test
    public void leaveOneArrayAsIs() {
        byte[] arrayOne = "ciao".getBytes();

        assertArrayEquals("ciao".getBytes(), ArrayOperations.concatenateData(arrayOne));
    }

    @Test
    public void concatenateTwoArrays() {
        byte[] arrayOne = "ciao".getBytes();
        byte[] arrayTwo = "my name is jumbotron".getBytes();

        assertArrayEquals("ciaomy name is jumbotron".getBytes(), ArrayOperations.concatenateData(arrayOne, arrayTwo));
    }

    @Test
    public void concatenateMultipleArrays() {
        byte[] arrayOne = "ciao".getBytes();
        byte[] arrayTwo = "my name is jumbotron".getBytes();
        byte[] arrayThree = "and I love CSS".getBytes();

        assertArrayEquals("ciaomy name is jumbotronand I love CSS".getBytes(), ArrayOperations.concatenateData(arrayOne, arrayTwo, arrayThree));
    }
}
