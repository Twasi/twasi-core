package net.twasi.core.interfaces.twitch;

import net.twasi.core.events.OutgoingMessageEvent;
import net.twasi.core.events.TwasiEventHandler;
import net.twasi.core.interfaces.api.CommunicationHandler;

import java.util.ArrayList;
import java.util.List;

public class TwitchCommunicationHandler extends CommunicationHandler {

    private List<TwasiEventHandler<OutgoingMessageEvent>> outgoingMessageHandlers = new ArrayList<>();

    TwitchCommunicationHandler(TwitchInterface inf) {
        super(inf);
    }

    @Override
    public boolean sendMessage(String message) {
        outgoingMessageHandlers.forEach(handler -> new Thread(() -> handler.on(new OutgoingMessageEvent(message, null))).start());

        return sendMessageInternal(message);
    }

    @Override
    public boolean sendInsecureMessage(String message) {
        getInterface().getBot().sendIRC().message(getInterface().getStreamer().getUser().getTwitchAccount().getChannel(), message);
        return true;
    }

    @Override
    public boolean sendMessageInternal(String message) {
        if (message.startsWith("/") && !message.toLowerCase().startsWith("/me") || message.startsWith(".")) {
            message = "7" + message.substring(1);
        }
        getInterface().getBot().sendIRC().message(getInterface().getStreamer().getUser().getTwitchAccount().getChannel(), message);
        return true;
    }

    public void registerOutgoingMessageHandler(TwasiEventHandler<OutgoingMessageEvent> e) {
        outgoingMessageHandlers.add(e);
    }

    /* @Override
    public boolean sendRawMessage(String rawMessage) {
        TwasiLogger.log.trace("IRC OUT: " + rawMessage);
        TwitchInterface twitchInterface = (TwitchInterface) getInterface();
        twitchInterface.getCommunicationHandler().sendRawMessage(rawMessage);
        return true;
    }*/

}
