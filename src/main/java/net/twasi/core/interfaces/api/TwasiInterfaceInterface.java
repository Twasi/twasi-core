package net.twasi.core.interfaces.api;

import net.twasi.core.models.Streamer;

public interface TwasiInterfaceInterface {

    void onEnable();
    void onDisable();

    boolean connect();

    boolean disconnect();

    boolean join(Streamer streamer);

    CommunicationHandlerInterface getCommunicationHandler();

}
