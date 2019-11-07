package net.twasi.core.api.ws;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebsocketServer extends WebSocketServer {

    private List<TwasiWebsocketClient> clients = new ArrayList<>();

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        clients.add(new TwasiWebsocketClient(webSocket, null));
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        clients = clients.stream().filter(el -> !el.getConnection().equals(webSocket)).collect(Collectors.toList());
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        try {
            JsonObject element = new JsonParser().parse(s).getAsJsonObject();
            switch(element.get("action").getAsString()) {
                case "subscribe":
                    break;
                case "unsubscribe":
                    break;
                case "authenticate":
                    break;
            }
        } catch (JsonParseException e) {

        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }
}
