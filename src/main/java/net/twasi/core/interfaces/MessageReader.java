package net.twasi.core.interfaces;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.MessageType;
import net.twasi.core.models.Message.TwasiMessage;
import org.pircbotx.hooks.events.MessageEvent;

public class MessageReader {
    private TwasiInterface twasiInterface;

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

    public void onMessage(MessageEvent event) {
        TwasiMessage message = TwasiMessage.from(event, twasiInterface);

        TwasiLogger.log.trace("IRC: message=" + message.getMessage() + ", type=" + message.getType() + ", sender=" + message.getSender());
        if (message.getType().equals(MessageType.PING)) {
            //twasiInterface.getCommunicationHandler().send"PONG " + message.getMessage());
            //TwasiLogger.log.debug("PING answered: " + message.getMessage());
            return;
        }
        twasiInterface.getDispatcher().dispatch(message);
    }
}
