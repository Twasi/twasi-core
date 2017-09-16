package net.twasi.core.interfaces;

import net.twasi.core.models.Message;

public interface CommunicationHandler {

    boolean sendMessage(Message message);

}
