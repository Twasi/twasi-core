package net.twasi.core.models.Message;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.permissions.PermissionGroups;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.messages.variables.VariablePreprocessor;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.Collections;

public class TwasiMessage {

    protected MessageType type;
    protected String message;
    protected TwitchAccount sender;
    private TwasiInterface twasiInterface;

    public TwasiMessage(String message, MessageType type, TwitchAccount sender, TwasiInterface inf) {
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
        return message != null && message.startsWith(ServiceRegistry.get(ConfigService.class).getCatalog().bot.prefix);
    }

    public TwasiCommand toCommand() {
        if (!isCommand()) {
            return null;
        }
        return new TwasiCommand(message, type, sender, twasiInterface);
    }

    public TwasiInterface getTwasiInterface() {
        return twasiInterface;
    }

    public void reply(String text) {
        text = VariablePreprocessor.process(getTwasiInterface(), text, this);
        twasiInterface.getCommunicationHandler().sendMessage(text);
    }

    public static TwasiMessage parse(String ircLine, TwasiInterface inf) {
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
                    message = parts[3].split(":", 2)[1];
                    String senderName = parts[1].split("!")[0].substring(1);

                    // Every user is a viewer if we parse a message from him. TwitchID will be set when tags are parsed
                    sender = new TwitchAccount(senderName, null, null, null, new ArrayList<>(Collections.singleton(PermissionGroups.VIEWER)));

                    String tags = parts[0].split("@")[1];
                    String[] splittedTags = tags.split(";");
                    for (String tag : splittedTags) {
                        String[] keyValue = tag.split("=");
                        if (keyValue.length == 2) {
                            switch (keyValue[0]) {
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

        return new TwasiMessage(message, type, sender, inf);
    }

    public static TwasiMessage from(MessageEvent event, TwasiInterface inf) {
        String message = event.getMessage();
        MessageType type = MessageType.PRIVMSG;
        TwitchAccount sender = new TwitchAccount(event.getUser().getNick(), event.getTags().get("display-name"), null, event.getTags().get("user-id"), new ArrayList<>(Collections.singleton(PermissionGroups.VIEWER)));

        if (event.getTags().get("mod").equals("1")) {
            if (!sender.getGroups().contains(PermissionGroups.MODERATOR))
                sender.getGroups().add(PermissionGroups.MODERATOR);
            if (!sender.getGroups().contains(PermissionGroups.SUBSCRIBERS))
                sender.getGroups().add(PermissionGroups.SUBSCRIBERS);
        }

        if (event.getTags().get("subscriber").equals("1")) {
            if (!sender.getGroups().contains(PermissionGroups.MODERATOR))
                sender.getGroups().add(PermissionGroups.SUBSCRIBERS);
        }

        if (event.getTags().get("badges").contains("broadcaster")) {
            if (!sender.getGroups().contains(PermissionGroups.BROADCASTER))
                sender.getGroups().add(PermissionGroups.BROADCASTER);
            if (!sender.getGroups().contains(PermissionGroups.MODERATOR))
                sender.getGroups().add(PermissionGroups.MODERATOR);
            if (!sender.getGroups().contains(PermissionGroups.SUBSCRIBERS))
                sender.getGroups().add(PermissionGroups.SUBSCRIBERS);
        }

        return new TwasiMessage(message, type, sender, inf);
    }
}