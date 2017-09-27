package net.twasi.core.webinterface;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class WebInterfaceApp {

        public static void start() {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
                server.createContext("/api", new ApiHandler());
                server.createContext("/", new StaticHandler());
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
