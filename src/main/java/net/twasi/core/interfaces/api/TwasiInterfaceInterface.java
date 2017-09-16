package net.twasi.core.interfaces.api;

import net.twasi.core.models.Streamer;

public interface TwasiInterfaceInterface {

    void onEnable();
    void onDIsable();

    boolean connect();

    boolean disconnect();

    boolean join(Streamer user);

    CommunicationHandlerInterface getCommunicationHandler();

}
