package net.twasi.core.plugin.api;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class TwasiCustomCommandEvent extends TwasiCommandEvent {

    private TwitchAccount sender;
    private Streamer streamer;
    private String usedCommandName;
    private List<String> args;

    public TwasiCustomCommandEvent(TwasiCommand command) {
        super(command);
        this.sender = command.getSender();
        this.streamer = command.getTwasiInterface().getStreamer();
        this.usedCommandName = command.getCommandName();
        this.args = new ArrayList<>(asList(command.getMessage().split(" "))); // As ArrayList to prevent UnsupportedOperationException in next line
        this.args.remove(0); // Remove command name from args
    }

    public void reply(String text) {
        getCommand().reply(text);
    }

    public TwitchAccount getSender() {
        return sender;
    }

    public Streamer getStreamer() {
        return streamer;
    }

    public String getUsedCommandName() {
        return usedCommandName;
    }

    public List<String> getArgs() {
        return args;
    }

    public String getArgsAsOne() {
        StringBuilder connected = new StringBuilder();
        for (String s : getArgs()) connected.append(" ").append(s);
        String string = connected.toString();
        if (string.equals("")) return null;
        else return string.substring(1); // Substring to remove leading whitespace
    }
}
