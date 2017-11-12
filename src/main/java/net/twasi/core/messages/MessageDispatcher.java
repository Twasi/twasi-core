package net.twasi.core.messages;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.Message;

public class MessageDispatcher {

    private TwasiInterface twasiInterface;

    public MessageDispatcher(TwasiInterface inf) {
        this.twasiInterface = inf;
    }

    public boolean dispatch(Message msg) {
        TwasiLogger.log.debug("Dispatching");
        return false;
    }

}
