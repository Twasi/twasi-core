package net.twasi.core.api.ws;

import com.google.gson.*;
import net.twasi.core.api.ws.models.TwasiWebsocketAnswer;
import net.twasi.core.api.ws.models.TwasiWebsocketClient;
import net.twasi.core.api.ws.models.WebsocketHandledException;
import net.twasi.core.logger.TwasiLogger;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@WebSocket
public class TwasiWebsocketServlet extends WebSocketServlet {

    public static final TwasiWebsocketTopicManager topicManager = new TwasiWebsocketTopicManager();
    private static List<TwasiWebsocketClient> clients = new ArrayList<>();
    private Session session;

    public TwasiWebsocketServlet() {
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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> clients.forEach(c -> {
            c.getConnection().close(new CloseStatus(1012, "Twasi Core is shutting down. This hasn't to do with you, my friend. Maybe it's restarting, we will find out!")); // 1012 = Service restarting
        })));

        TwasiLogger.log.info("Websocket API listening.");
    }

    private void broadcast(String s) {
        clients.forEach(c -> {
            try {
                c.send(s);
            } catch (IOException e) {
            }
        });
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.register(getClass());
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        clients = clients.stream().filter(el -> !el.getConnection().equals(session)).collect(Collectors.toList());
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        TwasiLogger.log.error("Websocket API error: ", t);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        this.session = session;
        TwasiWebsocketClient client = new TwasiWebsocketClient(session, null);
        clients.add(client);
        client.send(TwasiWebsocketAnswer.success(new JsonPrimitive("Connection established sucessfully")).toString());
    }

    @OnWebSocketMessage
    public void onMessage(String s) throws IOException {
        String ref = null;
        JsonElement result;
        try {
            JsonObject element = new JsonParser().parse(s).getAsJsonObject();
            if (element.has("ref")) ref = element.get("ref").getAsString();
            result = topicManager.handle(clients.stream().filter(c -> c.getConnection().equals(session)).findFirst().orElse(null), element);
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

        session.getRemote().sendString(response.toString());
    }

}
