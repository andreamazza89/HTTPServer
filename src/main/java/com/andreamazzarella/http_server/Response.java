package com.andreamazzarella.http_server;

class Response {
    static final String NEWLINE = "\n";
    static final String END_OF_HEADERS = NEWLINE;

    static final String STATUS_TWO_HUNDRED = "HTTP/1.1 200 OK" + NEWLINE;
    static final String STATUS_THREE_OH_TWO = "HTTP/1.1 302 Found" + NEWLINE;
    static final String STATUS_FOUR_OH_FOUR = "HTTP/1.1 404 Not Found" + NEWLINE;
    static final String STATUS_FOUR_OH_FIVE = "HTTP/1.1 405 Not Allowed" + NEWLINE;
    static final String STATUS_FOUR_ONE_EIGHT = "HTTP/1.1 418 I'm a Teapot" + NEWLINE;

    static final String CONTENT_TYPE_HEADER_NAME = "Content-Type: ";

    static final String TEA_POT_RESPONSE = STATUS_FOUR_ONE_EIGHT + NEWLINE + "I'm a teapot";
    static final String NOT_ALLOWED_RESPONSE = STATUS_FOUR_OH_FIVE + NEWLINE;
    static final String NOT_FOUND_RESPONSE = STATUS_FOUR_OH_FOUR + END_OF_HEADERS;
}

























































//       switch (request.method()) {
//           case GET:
//               if (resource.isRedirect()) {
//                   return "HTTP/1.1 302 Found\nLocation: " + resource.redirectLocation() + "\n\n";
//               } else {
//                   File file = new File("resources/form");
//                   FileInputStream fis;
//                   byte[] data = new byte[(int) file.length()];
//                   try {
//                       fis = new FileInputStream(file);
//                       fis.read(data);
//                       fis.close();
//                   } catch (FileNotFoundException e) {
//                       e.printStackTrace();
//                   } catch (IOException e) {
//                       e.printStackTrace();
//                   }
//
//                   return "HTTP/1.1 200 OK\n\n" + new String(data);
//               }
//           case OPTIONS:
//               Request.Method[] methods = resource.methodsAllowed();
//               String methodsAllowed = "";
//               for (Request.Method method : methods) {
//                   methodsAllowed += method.toString() + ",";
//               }
//               methodsAllowed = methodsAllowed.substring(0, methodsAllowed.length() - 1);
//               return "HTTP/1.1 200 OK\nAllow: " + methodsAllowed + "\n\n";
//           case POST:
//               String requestBody = request.getContent();
//               URI resourceUri = request.uri();
//               File file = new File("resources" + resourceUri.toString());
//               String path;
//               try {
//                   path = file.getCanonicalPath();
//                   PrintWriter printWriter = new PrintWriter(path);
//                   printWriter.print(requestBody);
//                   printWriter.close();
//               } catch (IOException e) {
//                   e.printStackTrace();
//               }
//
//               return "HTTP/1.1 200 OK\n\n";
//           default:
//               /////////////////////////////////////////////////////////////////////////////////////////////
//               /////////////////////////////////////////////////////////////////////////////////////////////
//               return "PLEASE HANDLE THIS CASE, PROBS SHOULD BE AN INTERNAL ERROR, RETRY OR SOMETHING";
//               /////////////////////////////////////////////////////////////////////////////////////////////
//               /////////////////////////////////////////////////////////////////////////////////////////////
//       }
//













////       switch (request.method()) {
////           case GET:
////               return GetResponse.respond(resource, somethingToHandleFileSystem)
////           case OPTIONS:
////                return OptionsResponse.respond(resource)
////           case POST:
////                return PostResponse.respond(resource, request, somethingToHandleFileSystem)
////           default:
////               return "PLEASE HANDLE THIS CASE, PROBS SHOULD BE AN INTERNAL ERROR, RETRY OR SOMETHING";
////       }
//   }
//}
