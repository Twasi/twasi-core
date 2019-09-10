package net.twasi.core.plugin.api.javascript;

import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.customcommands.TwasiCustomCommandEvent;
import net.twasi.core.plugin.api.customcommands.TwasiPluginCommand;

import javax.script.ScriptEngine;

import static net.twasi.core.plugin.api.javascript.JSPluginLoader.*;

public class JSPluginCommand extends TwasiPluginCommand {

    private final ScriptEngine env;

    public JSPluginCommand(TwasiUserPlugin twasiUserPlugin, ScriptEngine env) {
        super(twasiUserPlugin);
        this.env = env;
    }

    @Override
    public String getCommandName() {
        return getObject(env, "commandName");
    }

    @Override
    protected boolean execute(TwasiCustomCommandEvent event) {
        return invokeObj(env, "commandExecute", event);
    }


}
