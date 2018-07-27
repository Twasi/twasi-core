package net.twasi.core.interfaces.twitch;

import net.twasi.core.database.models.User;
import net.twasi.core.interfaces.api.CommunicationHandlerInterface;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Streamer;

public class TwitchConnectorInterface extends TwasiInterface {
    public TwitchConnectorInterface(User user) {
        super(user);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    /**
     * This subscribes for incoming messages to twasi-twitch-connector.
     * @return
     */
    @Override
    public boolean connect() {
        return false;
    }

    @Override
    public boolean disconnect() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public CommunicationHandlerInterface getCommunicationHandler() {
        return null;
    }

    @Override
    public Streamer getStreamer() {
        return null;
    }
}
