package net.twasi.core.interfaces;

import net.twasi.core.interfaces.twitch.TwitchInterface;
import net.twasi.core.models.Streamer;

class InterfaceLoader {

    public InterfaceLoader() {
    }

    public TwitchInterface createNewTwitch(Streamer streamer) {
        return new TwitchInterface(streamer);
    }
}
