package net.twasi.core.api.ws;

import com.google.gson.*;
import net.twasi.core.api.ws.models.TwasiWebsocketAnswer;
import net.twasi.core.api.ws.models.TwasiWebsocketClient;
import net.twasi.core.logger.TwasiLogger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class WebsocketServer extends WebSocketServer {

    private List<TwasiWebsocketClient> clients = new ArrayList<>();
    private TwasiWebsocketTopicManager topicManager = new TwasiWebsocketTopicManager();

    public WebsocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        clients.add(new TwasiWebsocketClient(webSocket, null));
        webSocket.send(TwasiWebsocketAnswer.success(new JsonPrimitive("Connection established sucessfully")).toString());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        clients = clients.stream().filter(el -> !el.getConnection().equals(webSocket)).collect(Collectors.toList());
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        String ref = null;
        JsonElement result;
        try {
            JsonObject element = new JsonParser().parse(s).getAsJsonObject();
            if (element.has("ref")) ref = element.get("ref").getAsString();
            result = topicManager.handle(clients.stream().filter(c -> c.getConnection().equals(webSocket)).findFirst().orElse(null), element);
        } catch (JsonParseException e) {
            JsonObject ob = new JsonObject();
            ob.add("status", new JsonPrimitive("INVALID_INPUT"));
            ob.add("description", new JsonPrimitive("Please send a valid JSON string."));
            result = ob;
        } catch (WebsocketHandledException e) {
            JsonObject ob = new JsonObject();
            ob.add("status", new JsonPrimitive("ERROR"));
            ob.add("description", new JsonPrimitive("A known Error occurred: " + e.getMessage() + " Error ref-id: " /* TODO Add ref id */));
            result = ob;
        } catch (Exception e) {
            JsonObject ob = new JsonObject();
            ob.add("status", new JsonPrimitive("ERROR"));
            ob.add("description", new JsonPrimitive("An Error occurred. If this keeps happening please inform the team with error ref-id: " /* TODO Add ref id */));
            result = ob;
        }
        JsonObject response = new JsonObject();
        if (ref != null) response.add("ref", new JsonPrimitive(ref));
        else response.add("ref", null);
        response.add("result", result);

        webSocket.send(response.toString());
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        TwasiLogger.log.error("Websocket API error: ", e);
    }

    @Override
    public void onStart() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JsonObject ob = new JsonObject();
                ob.add("type", new JsonPrimitive("keepalive"));
                ob.add("timestamp", new JsonPrimitive(Calendar.getInstance().getTime().getTime()));
                broadcast(ob.toString());
            }
        });
        thread.setDaemon(true);
        thread.start();

        JsonObject ob = new JsonObject();
        ob.add("type", new JsonPrimitive("shutdown"));
        ob.add("reason", new JsonPrimitive("Twasi Core is shutting down. This hasn't to do with you, my friend. Maybe it's restarting, we will find out!"));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.clients.forEach(c -> {
            c.getConnection().send(ob.toString());
            c.getConnection().close(1012); // 1012 = Service restarting
        })));

        TwasiLogger.log.info("Websocket API listening.");
    }

    public TwasiWebsocketTopicManager getTopicManager() {
        return topicManager;
    }
}
