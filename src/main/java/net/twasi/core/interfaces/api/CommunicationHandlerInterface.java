package net.twasi.core.interfaces.api;

public interface CommunicationHandlerInterface {

    boolean sendMessage(String message);
    boolean sendInsecureMessage(String message);

    // boolean sendRawMessage(String rawMessage);

}
