package com.andreamazzarella.http_server;

import java.io.File;
import java.net.URI;

public class DirectoryExplorer {

    private static final String HTML_LINK_TEMPLATE = "<a href=\"/%s\">%s</a>\n";
    private static final String HTML_PARAGRAPH_TEMPLATE = "<p>%s</p>\n";
    private static final String HTML_LISTING_TEMPLATE =
            "<!doctype html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "%s" +
                    "</body>\n" +
                    "</html>";

    private final File directory;

    public DirectoryExplorer(URI directoryPath) {
        this.directory = new File(directoryPath.getPath());
    }

    public String generateHTMLListing() {
        String[] fileNames = directory.list();
        String HTMLBodyContent;

        if (fileNames.length == 0) {
             HTMLBodyContent = String.format(HTML_PARAGRAPH_TEMPLATE, "No files were found");
        } else {
            HTMLBodyContent = createLinksToFiles(fileNames);
        }

        return String.format(HTML_LISTING_TEMPLATE, HTMLBodyContent);
    }

    private String createLinksToFiles(String[] fileNames) {
        String links = "";
        for (String fileName : fileNames) {
            links += String.format(HTML_LINK_TEMPLATE, fileName, fileName);
        }
        return links;
    }
}
