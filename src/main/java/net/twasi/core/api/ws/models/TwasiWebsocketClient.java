package net.twasi.core.api.ws.models;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class TwasiWebsocketClient {

    private Session connection;
    private TwasiWebsocketAuthentication authentication;

    public TwasiWebsocketClient(Session connection, TwasiWebsocketAuthentication authentication) {
        this.connection = connection;
        this.authentication = authentication;
    }

    public Session getConnection() {
        return connection;
    }

    public TwasiWebsocketAuthentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(TwasiWebsocketAuthentication authentication) {
        this.authentication = authentication;
    }

    public void send(String s) throws IOException {
        this.connection.getRemote().sendString(s);
    }
}
