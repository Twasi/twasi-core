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
        if (message.startsWith("/") || message.startsWith(".")) {
            message = "7" + message.substring(1);
        }
        getInterface().getBot().sendIRC().message(getInterface().getStreamer().getUser().getTwitchAccount().getChannel(), message);
        return true;
    }

    @Override
    public boolean sendInsecureMessage(String message) {
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
