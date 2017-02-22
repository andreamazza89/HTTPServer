package com.andreamazzarella.http_server;

import java.util.Optional;

class GetResponse {
    static String respond(Resource route, Blaah blaah) {
        if (route.isRedirect()) {
            return "HTTP/1.1 302 Found\nLocation: " + route.redirectLocation() + "\n\n";
        } else {
            Optional<String> resource = blaah.getResource(route.uri());


//            File file = new File("resources/form");
//            FileInputStream fis;
//            byte[] data = new byte[(int).length()];
//            try {
//                fis = new FileInputStream(file);
//                fis.read(data);
//                fis.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return "HTTP/1.1 200 OK\n\n" + resource.orElse("");
        }
    }
}
