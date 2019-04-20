package net.twasi.core.plugin.api.customcommands;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.translations.renderer.TranslationRenderer;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class TwasiCustomCommandEvent extends TwasiCommandEvent {

    private TwitchAccount sender;
    private Streamer streamer;
    private String usedCommandName;
    private List<String> args;
    private ClassLoader loader;

    public TwasiCustomCommandEvent(TwasiCommand command, ClassLoader loader) {
        super(command);
        this.sender = command.getSender();
        this.streamer = command.getTwasiInterface().getStreamer();
        this.usedCommandName = command.getCommandName();
        this.args = new ArrayList<>(asList(command.getMessage().split(" "))); // As ArrayList to prevent UnsupportedOperationException in next line
        this.loader = loader;
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

    public boolean hasArgs() {
        return getArgs().size() > 0;
    }

    public boolean hasPermission(String key) {
        return this.streamer.getUser().hasPermission(this.sender, key);
    }

    public TranslationRenderer getRenderer(TranslationRenderer copyBindings) {
        return TranslationRenderer
                .getInstance(loader, streamer.getUser().getConfig().getLanguage())
                .bindAll(copyBindings.getBindings())
                .bindAllObjects(copyBindings.getObjectBindings())
                .bindObject("user", streamer.getUser().getTwitchAccount())
                .bindObject("streamer", streamer.getUser().getTwitchAccount())
                .bindObject("sender", sender)
                .bind("command", getUsedCommandName())
                .bind("args", getArgsAsOne());
    }

    public TranslationRenderer getRenderer(){
        return getRenderer(
                TranslationRenderer.getInstance(null, null) // Can be null, will be replaced by the other method any way
        );
    }
}
