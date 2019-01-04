package net.twasi.core.interfaces.api;

public interface CommunicationHandlerInterface {

    boolean sendMessage(String message);
    boolean sendInsecureMessage(String message);
    boolean sendMessageInternal(String message);

    // boolean sendRawMessage(String rawMessage);

}
