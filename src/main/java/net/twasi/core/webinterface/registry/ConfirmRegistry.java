package net.twasi.core.webinterface.registry;

import com.sun.net.httpserver.HttpServer;
import net.twasi.core.webinterface.controller.confirm.ConfirmController;

public class ConfirmRegistry {

    public static void register (HttpServer httpServer) {

        // Confirm
        httpServer.createContext("/confirm", new ConfirmController());

    }

}
