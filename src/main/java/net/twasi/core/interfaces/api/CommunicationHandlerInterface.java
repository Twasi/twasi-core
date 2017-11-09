package net.twasi.core.interfaces.api;

import net.twasi.core.models.Message.Message;

public interface CommunicationHandlerInterface {

    boolean sendMessage(Message message);

    Message readMessage();

}
