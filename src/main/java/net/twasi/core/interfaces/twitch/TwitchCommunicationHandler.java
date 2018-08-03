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
        return sendRawMessage("PRIVMSG " + getInterface().getStreamer().getUser().getTwitchAccount().getChannel() + " :" + message);
    }

    @Override
    public boolean sendRawMessage(String rawMessage) {
        try {
            TwasiLogger.log.trace("IRC OUT: " + rawMessage);
            TwitchInterface twitchInterface = (TwitchInterface) getInterface();
            twitchInterface.getWriter().write(rawMessage + "\n");
            twitchInterface.getWriter().flush();
            return true;
        } catch (IOException e) {
            TwasiLogger.log.error(e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TwasiMessage readMessage() {
        TwitchInterface twitchInterface = (TwitchInterface) getInterface();

        try {
            String line = twitchInterface.getReader().readLine();
            TwasiLogger.log.trace("IRC IN: " + line);
            if (line == null) {
                return null;
            }
            return TwasiMessage.parse(line, getInterface());
        } catch (IOException e) {
            if (e.getMessage().equals("Socket closed")) {
                TwasiLogger.log.info("Connection to Socket lost for Interface " + twitchInterface.getStreamer().getUser().getTwitchAccount().getUserName());
                return null;
            }

            TwasiLogger.log.error(e);
            e.printStackTrace();
            return null;
        }
    }

}
