package net.twasi.core.models.Message;

import net.twasi.core.config.Config;
import net.twasi.core.database.Database;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.permissions.PermissionGroups;
import net.twasi.core.interfaces.api.TwasiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Message {

    protected MessageType type;
    protected String message;
    protected TwitchAccount sender;
    protected TwasiInterface twasiInterface;

    public Message(String message, MessageType type, TwitchAccount sender, TwasiInterface inf) {
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

    public TwitchAccount getSender() {
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

    public TwasiInterface getTwasiInterface() {
        return twasiInterface;
    }

    public void reply(String text) {
        twasiInterface.getCommunicationHandler().sendMessage(text);
    }

    public static Message parse(String ircLine, TwasiInterface inf) {
        // System.out.println(ircLine);
        String message = null;
        TwitchAccount sender = null;
        MessageType type = null;

        if (ircLine.toLowerCase().startsWith("ping ")) {
            type = MessageType.PING;
            message = ircLine.substring(5);
        } else {
            String[] parts = ircLine.split(" ", 4);
            if (parts.length == 4) {
                if (parts[2].equalsIgnoreCase("PRIVMSG")) {
                    type = MessageType.PRIVMSG;
                    message = parts[3].split(":")[1];
                    String senderName = parts[1].split("!")[0].substring(1);

                    // Every user is a viewer if we parse a message from him. TwitchID will be set when tags are parsed
                    sender = new TwitchAccount(senderName, null, null, new ArrayList<>(Collections.singleton(PermissionGroups.VIEWER)));

                    String tags = parts[0].split("@")[1];
                    String[] splittedTags = tags.split(";");
                    for (String tag : splittedTags) {
                        String[] keyValue = tag.split("=");
                        if (keyValue.length == 2) {
                            switch(keyValue[0]) {
                                case "mod":
                                    if (keyValue[1].equals("1")) {
                                        if (!sender.getGroups().contains(PermissionGroups.MODERATOR))
                                            sender.getGroups().add(PermissionGroups.MODERATOR);
                                        if (!sender.getGroups().contains(PermissionGroups.SUBSCRIBERS))
                                            sender.getGroups().add(PermissionGroups.SUBSCRIBERS);
                                    }
                                    break;
                                case "subscriber":
                                    if (keyValue[1].equals("1")) {
                                        if (!sender.getGroups().contains(PermissionGroups.SUBSCRIBERS))
                                            sender.getGroups().add(PermissionGroups.SUBSCRIBERS);
                                    }
                                    break;
                                case "user-id":
                                    sender.setTwitchId(keyValue[1]);
                                    break;
                                case "badges":
                                    if (keyValue[1].startsWith("broadcaster")) {
                                        if (!sender.getGroups().contains(PermissionGroups.BROADCASTER))
                                            sender.getGroups().add(PermissionGroups.BROADCASTER);
                                        if (!sender.getGroups().contains(PermissionGroups.MODERATOR))
                                            sender.getGroups().add(PermissionGroups.MODERATOR);
                                        if (!sender.getGroups().contains(PermissionGroups.SUBSCRIBERS))
                                            sender.getGroups().add(PermissionGroups.SUBSCRIBERS);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }

        if (type == null) {
            return null;
        }

        return new Message(message, type, sender, inf);
    }
}