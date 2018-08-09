package net.twasi.core.interfaces.api;

import net.twasi.core.models.Message.TwasiMessage;

public interface CommunicationHandlerInterface {

    boolean sendMessage(String message);
    boolean sendInsecureMessage(String message);

    // boolean sendRawMessage(String rawMessage);

}
