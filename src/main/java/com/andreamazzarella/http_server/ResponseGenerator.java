package com.andreamazzarella.http_server;

import java.io.*;
import java.net.URI;
import java.util.Arrays;

class ResponseGenerator {
   static final String FOUR_OH_FOUR = "HTTP/1.1 404 Not Found\n\n";
   static final String TWO_HUNDRED = "HTTP/1.1 200 OK\n\n";

   static String createResponse(Request request, Route route) {

       if (!Arrays.asList(route.methodsAllowed()).contains(request.method())) {
           return FOUR_OH_FOUR;
       }

       switch (request.method()) {
           case GET:
               if (route.isRedirectRoute()) {
                   return "HTTP/1.1 302 Found\nLocation: " + route.redirectLocation() + "\n\n";
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

                   return "HTTP/1.1 200 OK\n\n" + new String(data);
               }
           case OPTIONS:
               Request.Method[] methods = route.methodsAllowed();
               String methodsAllowed = "";
               for (Request.Method method : methods) {
                   methodsAllowed += method.toString() + ",";
               }
               methodsAllowed = methodsAllowed.substring(0, methodsAllowed.length() - 1);
               return "HTTP/1.1 200 OK\nAllow: " + methodsAllowed + "\n\n";
           case POST:
               String requestBody = request.body();
               URI resourceUri = request.uri();
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

               return "HTTP/1.1 200 OK\n\n";
           default:
               /////////////////////////////////////////////////////////////////////////////////////////////
               /////////////////////////////////////////////////////////////////////////////////////////////
               return "PLEASE HANDLE THIS CASE, PROBS SHOULD BE AN INTERNAL ERROR, RETRY OR SOMETHING";
               /////////////////////////////////////////////////////////////////////////////////////////////
               /////////////////////////////////////////////////////////////////////////////////////////////
       }
   }
}
