package net.twasi.core.webinterface;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.webinterface.api.ApiHandler;
import net.twasi.core.webinterface.api.ApiRegistry;

import java.net.InetSocketAddress;

public class WebInterfaceApp {

        public static void start() {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
                //server.createContext("/api", new ApiHandler());
                ApiRegistry.register(server);
                server.createContext("/", new StaticHandler());
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
