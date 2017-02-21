package com.andreamazzarella.http_server;

import java.io.*;
import java.net.URI;
import java.util.*;

public class Routes {

    private List<Route> routes = new ArrayList<>();

    void addRoute(Route route) {
        routes.add(route);
    }

    public String generateResponse(Request request) {
        URI resource = URI.create(request.uri());
        Optional<Route> route = findRoute(resource);
        String response = "";


//        look for route, if there, ask it to genereateREsponse for request, if not, return 404;

        if (!route.isPresent()) {
            response = "HTTP/1.1 404 Not Found\n\n";
        } else if (request.method() == Request.Method.OPTIONS) {
            Request.Method[] methods = route.get().methodsAllowed();
            String methodsAllowed = "";
            for (Request.Method method : methods) {
                methodsAllowed += method.toString() + ",";
            }
            methodsAllowed = methodsAllowed.substring(0, methodsAllowed.length()-1);
            response = "HTTP/1.1 200 OK\nAllow: " + methodsAllowed + "\n\n";
        } else if (request.method() == Request.Method.GET) {
            if (route.get().isRedirectRoute()) {
                response = "HTTP/1.1 302 Found\nLocation: "+route.get().redirectLocation()+"\n\n";
            } else {
                // retrieve the message body and then add it to the 200
                File file = new File("resources/form");
                FileInputStream fis;
                byte[] data = new byte[(int) file.length()];
                try {
                    fis = new FileInputStream(file);
                    fis.read(data);
                    fis.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                response = "HTTP/1.1 200 OK\n\n" + new String(data);
            }
        } else if (request.method() == Request.Method.POST) {
            String requestBody = request.body();
            URI resourceUri = URI.create(request.uri());
            File file = new File("resources" + resourceUri.toString());
            String path;
            try {
                path = file.getCanonicalPath();
                PrintWriter printWriter = new PrintWriter(path);
                printWriter.print(requestBody);
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            response = "HTTP/1.1 200 OK\n\n";
        }

        return response;
    }

    private Optional<Route> findRoute(URI resource) {
        return routes.stream().filter((route) -> route.getResource().equals(resource)).findFirst();
    }

}
