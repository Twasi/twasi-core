package net.twasi.core.interfaces;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.Message;

public class MessageReader implements Runnable {
    private TwasiInterface twasiInterface;

    public MessageReader(TwasiInterface inf) {
        twasiInterface = inf;
    }

    @Override
    public void run() {
        while (true) {
            Message message = twasiInterface.getCommunicationHandler().readMessage();
            if (message == null) {
                continue;
            }
            TwasiLogger.log.info(message.getMessage() + ", " + message.getType() + ", " + message.getSender());
        }
    }
}
