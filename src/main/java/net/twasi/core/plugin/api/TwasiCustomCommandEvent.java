package net.twasi.core.plugin.api;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;

import java.util.Arrays;
import java.util.List;

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
        this.args = Arrays.asList(command.getMessage().split(" "));
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
}
