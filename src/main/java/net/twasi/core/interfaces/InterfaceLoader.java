package net.twasi.core.interfaces;

import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.models.Streamer;

class InterfaceLoader {

    public InterfaceLoader() {
    }

    /**
     * Creates new TwitchInterface for a streamer
     * @param streamer The streamer to create the interface for
     * @return the TwitchInterface created
     */
    public TwitchInterface createNewTwitch(Streamer streamer) {
        return new TwitchInterface(streamer);
    }
}
