package net.twasi.core.models.Message;

import net.twasi.core.config.Config;
import net.twasi.core.interfaces.api.TwasiInterface;

public class Message {

    protected MessageType type;
    protected String message;
    protected String sender;
    protected TwasiInterface twasiInterface;

    public Message(String message, MessageType type, String sender, TwasiInterface inf) {
        this.message = message;
        this.type = type;
        this.sender = sender;
        this.twasiInterface = inf;
    }

    public String getMessage() {
        return this.message;
    }

    public MessageType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public boolean isCommand() {
        if (message == null) {
            return false;
        }
        return message.startsWith(Config.getCatalog().bot.prefix);
    }

    public Command toCommand() {
        if (!isCommand()) {
            return null;
        }
        return new Command(message, type,sender, twasiInterface);
    }

    public void reply(String text) {
        twasiInterface.getCommunicationHandler().sendMessage("Hello World!");
    }

    public static Message parse(String ircLine, TwasiInterface inf) {
        // System.out.println(ircLine);
        String message = null;
        String sender = null;
        MessageType type = null;

        if (ircLine.toLowerCase().startsWith("ping ")) {
            type = MessageType.PING;
            message = ircLine.substring(5);
        } else {
            String[] parts = ircLine.split(" ", 3);
            if (parts.length == 3) {
                if (parts[1].equalsIgnoreCase("PRIVMSG")) {
                    type = MessageType.PRIVMSG;
                    message = parts[2].split(":")[1];
                    sender = parts[0].split("!")[0].substring(1);
                }
            }
        }

        if (type == null) {
            return null;
        }

        return new Message(message, type, sender, inf);
    }

}
