package net.twasi.core.api.ws.models;

import org.java_websocket.WebSocket;

public class TwasiWebsocketClient {

    private WebSocket connection;
    private TwasiWebsocketAuthentication authentication;

    public TwasiWebsocketClient(WebSocket connection, TwasiWebsocketAuthentication authentication) {
        this.connection = connection;
        this.authentication = authentication;
    }

    public WebSocket getConnection() {
        return connection;
    }

    public TwasiWebsocketAuthentication getAuthentication() {
        return authentication;
    }

}
