package net.twasi.core.interfaces.api;

import net.twasi.core.models.Message;

public interface CommunicationHandlerInterface {

    boolean sendMessage(Message message);

}
