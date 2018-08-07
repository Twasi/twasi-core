package net.twasi.core.interfaces.twitch;

import net.twasi.core.interfaces.api.CommunicationHandler;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message.TwasiMessage;

import java.io.IOException;

public class TwitchCommunicationHandler extends CommunicationHandler {

    TwitchCommunicationHandler(TwitchInterface inf) {
        super(inf);
    }

    @Override
    public boolean sendMessage(String message) {
        getInterface().getBot().sendIRC().message(getInterface().getStreamer().getUser().getTwitchAccount().getChannel(), message);
        return true;
    }

    /* @Override
    public boolean sendRawMessage(String rawMessage) {
        TwasiLogger.log.trace("IRC OUT: " + rawMessage);
        TwitchInterface twitchInterface = (TwitchInterface) getInterface();
        twitchInterface.getCommunicationHandler().sendRawMessage(rawMessage);
        return true;
    }*/

}
