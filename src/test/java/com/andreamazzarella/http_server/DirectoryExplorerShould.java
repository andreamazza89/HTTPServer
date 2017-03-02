package com.andreamazzarella.http_server;

import com.andreamazzarella.http_server.DirectoryExplorer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.TestCase.assertEquals;

public class DirectoryExplorerShould {

    @Test
    public void generateEmptyDocumentWithNoFilesInDirectory() throws IOException {
        Path directory = Files.createTempDirectory( "tmp");
        DirectoryExplorer directoryExplorer = new DirectoryExplorer(directory.toUri());
        String expectedListing =
                "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body>\n" +
                        "<p>No files were found</p>\n" +
                    "</body>\n" +
                "</html>";

        assertEquals(expectedListing, directoryExplorer.generateHTMLListing());

        new File(directory.toUri()).delete(); // double safety to ensure the temporary directory is deleted
    }

    @Test
    public void listAllFileNamesInADirectoryAsLinks() throws IOException {
        Path directory = Files.createTempDirectory( "tmp");
        String fileOneName = Files.createTempFile(directory, "file1", ".lol").getFileName().toString();
        String fileTwoName = Files.createTempFile(directory, "file2", ".cats").getFileName().toString();
        DirectoryExplorer directoryExplorer = new DirectoryExplorer(directory.toUri());

        String expectedListing = String.format(
                "<!doctype html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "</head>\n" +
                        "<body>\n" +
                            "<a href=\"/%s\">%s</a>\n" +
                            "<a href=\"/%s\">%s</a>\n" +
                        "</body>\n" +
                        "</html>", fileOneName, fileOneName, fileTwoName, fileTwoName);

        assertEquals(expectedListing, directoryExplorer.generateHTMLListing());

        new File(directory.toUri()).delete(); // double safety to ensure the temporary directory is deleted
    }



}
