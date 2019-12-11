package net.twasi.core.plugin.api.customcommands;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.translations.renderer.TranslationRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Arrays.asList;

public class TwasiCustomCommandEvent extends TwasiCommandEvent {

    private TwitchAccount sender;
    private Streamer streamer;
    private TwasiInterface twasiInterface;
    private String usedCommandName;
    protected List<String> args;
    private ClassLoader loader;
    private TranslationRenderer addBindings = null;

    public TwasiCustomCommandEvent(TwasiCommand command, ClassLoader loader) {
        super(command);
        this.sender = command.getSender();
        this.twasiInterface = command.getTwasiInterface();
        this.streamer = twasiInterface.getStreamer();
        this.usedCommandName = command.getCommandName();
        this.args = new ArrayList<>(asList(command.getMessage().split(" "))); // As ArrayList to prevent UnsupportedOperationException in next line
        this.loader = loader;
        this.args.remove(0); // Remove command name from args
    }

    public TwasiCustomCommandEvent(TwasiCommand command) {
        this(command, TwasiCustomCommandEvent.class.getClassLoader());
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
        if (string.equals("")) return "";
        else return string.substring(1); // Substring to remove leading whitespace
    }

    public boolean hasArgs() {
        return getArgs().size() > 0;
    }

    public boolean hasPermission(String key) {
        return this.streamer.getUser().hasPermission(this.sender, key);
    }

    public TranslationRenderer getRenderer(TranslationRenderer copyBindings, String folder) {
        TranslationRenderer renderer = TranslationRenderer
                .getInstance(loader, streamer.getUser().getConfig().getLanguage(), folder)
                .bindAll(copyBindings.getBindings())
                .bindAllObjects(copyBindings.getObjectBindings())
                .multiBindObject(streamer.getUser().getTwitchAccount(), "user", "streamer")
                .bindObject("sender", sender)
                .bind("command", getUsedCommandName())
                .bind("args", getArgsAsOne());
        AtomicInteger i = new AtomicInteger(1);
        args.forEach(arg -> renderer.bind("args." + i.getAndIncrement(), arg));
        return renderer;
    }

    public TwasiCustomCommandEvent addBindings(TranslationRenderer renderer) {
        this.addBindings = renderer;
        return this;
    }

    public TranslationRenderer getRenderer(String folder) {
        return getRenderer(
                (addBindings != null) ? addBindings : TranslationRenderer.getInstance(null, null, folder), // Can be null, will be replaced by the other method any way
                folder
        );
    }

    public final TranslationRenderer getRenderer() {
        return getRenderer("");
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public TwasiInterface getTwasiInterface() {
        return twasiInterface;
    }
}
