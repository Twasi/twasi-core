package net.twasi.core.interfaces;

import net.twasi.core.models.Streamer;

public interface TwasiInterface {

    boolean connect();

    boolean disconnect();

    boolean join(Streamer user);

    CommunicationHandler getCommunicationHandler();

}
