package net.twasi.core.webinterface;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.config.Config;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.webinterface.api.ApiHandler;
import net.twasi.core.webinterface.api.ApiRegistry;

import java.net.InetSocketAddress;

public class WebInterfaceApp {

        public static void start() {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(Config.catalog.webinterface.port), 0);
                //server.createContext("/api", new ApiHandler());
                ApiRegistry.register(server);
                server.createContext("/", new StaticHandler());
                server.start();
                TwasiLogger.log.info("Web interface started on port " + Config.catalog.webinterface.port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
