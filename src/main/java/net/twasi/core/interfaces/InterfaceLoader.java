package net.twasi.core.interfaces;

import net.twasi.core.interfaces.twitch.TwitchInterface;

class InterfaceLoader {

    TwitchInterface twitch;

    public InterfaceLoader() {
        twitch = new TwitchInterface();
    }

    public TwitchInterface getTwitch() {
        return twitch;
    }
}
