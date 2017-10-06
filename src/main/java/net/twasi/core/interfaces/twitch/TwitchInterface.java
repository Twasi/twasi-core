package net.twasi.core.interfaces.twitch;

import net.twasi.core.interfaces.api.CommunicationHandler;
import net.twasi.core.interfaces.api.CommunicationHandlerInterface;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Message;
import net.twasi.core.models.Streamer;

public class TwitchInterface extends TwasiInterface {

    CommunicationHandler handler = new CommunicationHandler() {
        @Override
        public boolean sendMessage(Message message) {
            return false;
        }
    };

    @Override
    public void onEnable() {
        TwasiLogger.log.debug("Enabling Twitch Interface.");
    }

    @Override
    public void onDisable() {
        TwasiLogger.log.debug("Disabling Twitch Interface.");
    }

    @Override
    public boolean connect() {
        TwasiLogger.log.info("Connecting to Twitch IRC");
        return true;
    }

    @Override
    public boolean disconnect() {
        TwasiLogger.log.info("Disconnecting from Twitch IRC");
        return false;
    }

    @Override
    public boolean join(Streamer streamer) {
        TwasiLogger.log.debug("Joining channel for Streamer " + streamer.getUser().getTwitchAccount().getUserName());
        return false;
    }

    @Override
    public CommunicationHandlerInterface getCommunicationHandler() {
        return handler;
    }
}
