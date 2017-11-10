package net.twasi.core.models.Message;

public class Message {

    private MessageType type;
    private String message;
    private String sender;

    public Message(String message, MessageType type, String sender) {
        this.message = message;
        this.type = type;
        this.sender = sender;
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

    public static Message parse(String ircLine) {
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

        return new Message(message, type, sender);
    }

}
