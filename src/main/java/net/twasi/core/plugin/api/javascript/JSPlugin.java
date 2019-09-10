package net.twasi.core.plugin.api.javascript;

import net.twasi.core.plugin.PluginConfig;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.*;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.io.File;
import java.util.List;

import static net.twasi.core.plugin.api.javascript.JSPluginLoader.invoke;

public class JSPlugin<T> extends TwasiPlugin<T> {

    private final ScriptEngine plugin;
    private final ScriptEngine userPlugin;
    private final List<ScriptEngine> commands;
    private final List<ScriptEngine> variables;
    private final File jsPluginFolder;

    public JSPlugin(
            ScriptEngine plugin,
            ScriptEngine userPlugin,
            List<ScriptEngine> commands,
            List<ScriptEngine> variables,
            PluginConfig description,
            File jsPluginFolder
    ) {
        super(new JSPluginTranslation(), description);

        Bindings pluginBindings = plugin.createBindings();
        pluginBindings.put("JavaPlugin", this);
        plugin.setBindings(pluginBindings, ScriptContext.GLOBAL_SCOPE);

        this.plugin = plugin;
        this.userPlugin = userPlugin;
        this.commands = commands;
        this.variables = variables;
        this.jsPluginFolder = jsPluginFolder;
    }

    @Override
    public void onDeactivate() {
        invoke(plugin, "onDeactivate");
    }

    @Override
    public void onActivate() {
        invoke(plugin, "onActivate");
    }

    @Override
    public void onReady() {
        invoke(plugin, "onReady");
    }

    @Override
    public Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return new TwasiUserPlugin() {
            @Override
            public void onEnable(TwasiEnableEvent e) {
                invoke(userPlugin, "onEnable", e);
                commands.forEach(env -> registerCommand(new JSPluginCommand(this, env)));
                variables.forEach(env -> registerVariable(new JSPluginVariable(this, env)));
            }

            @Override
            public void onDisable(TwasiDisableEvent e) {
                invoke(userPlugin, "onDisable", e);
            }

            @Override
            public void onInstall(TwasiInstallEvent e) {
                invoke(userPlugin, "onInstall", e);
            }

            @Override
            public void onUninstall(TwasiInstallEvent e) {
                invoke(userPlugin, "onUninstall", e);
            }

            @Override
            public void onCommand(TwasiCommandEvent e) {
                invoke(userPlugin, "onCommand", e);
            }

            @Override
            public void onMessage(TwasiMessageEvent e) {
                invoke(userPlugin, "onMessage", e);
            }
        }.getClass();
    }
}
