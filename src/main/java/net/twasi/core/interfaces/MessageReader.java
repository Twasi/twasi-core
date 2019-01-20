package net.twasi.core.interfaces;

import net.twasi.core.events.TwasiEventHandler;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.events.IncomingMessageEvent;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.TwasiMessage;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.List;

public class MessageReader {
    private TwasiInterface twasiInterface;

    // Event handlers
    private List<TwasiEventHandler<IncomingMessageEvent>> incomingMessageEventHandlers = new ArrayList<>();

    public MessageReader(TwasiInterface inf) {
        twasiInterface = inf;
    }

    /* @Override
    public void run() {
        while (!twasiInterface.getSocket().isClosed()) {
            try {
                TwasiMessage message = twasiInterface.getCommunicationHandler().readMessage();
                if (message == null) {
                    continue;
                }
                TwasiLogger.log.trace("IRC: message=" + message.getMessage() + ", type=" + message.getType() + ", sender=" + message.getSender());
                if (message.getType().equals(MessageType.PING)) {
                    twasiInterface.getCommunicationHandler().sendRawMessage("PONG");
                    TwasiLogger.log.debug("PING answered: " + message.getMessage());
                    continue;
                }
                twasiInterface.getDispatcher().dispatch(message);
            } catch (Throwable e) {
                TwasiLogger.log.error("Exception in MessageReader of " + twasiInterface.getStreamer().getUser().getTwitchAccount().getUserName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }*/

    public void registerIncomingMessageHandler(TwasiEventHandler<IncomingMessageEvent> e) {
        incomingMessageEventHandlers.add(e);
    }

    public void onMessage(MessageEvent event) {
        TwasiMessage message = TwasiMessage.from(event, twasiInterface);

        if (message.getSender().getUserName().equalsIgnoreCase(twasiInterface.getStreamer().getUser().getTwitchBotAccountOrDefault().getUserName())) {
            // Ignore our own messages
            return;
        }

        TwasiLogger.log.trace("IRC: message=" + message.getMessage() + ", type=" + message.getType() + ", sender=" + message.getSender());
        // Ping requests are handled by PircX
        //if (message.getType().equals(MessageType.PING)) {
            //twasiInterface.getCommunicationHandler().send("PONG " + message.getMessage());
            //TwasiLogger.log.debug("PING answered: " + message.getMessage());
            //return;
        //}

        // Call all event handlers (middleware)
        incomingMessageEventHandlers.forEach(handler -> new Thread(() -> handler.on(new IncomingMessageEvent(message))).start());

        twasiInterface.getDispatcher().dispatch(message);
    }
}
