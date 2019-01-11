package net.twasi.core.interfaces.api;

import net.twasi.core.events.OutgoingMessageEvent;
import net.twasi.core.events.TwasiEventHandler;

public interface CommunicationHandlerInterface {

    boolean sendMessage(String message);
    boolean sendInsecureMessage(String message);
    boolean sendMessageInternal(String message);

    // boolean sendRawMessage(String rawMessage);
    void registerOutgoingMessageHandler(TwasiEventHandler<OutgoingMessageEvent> e);

}
