package net.twasi.core.plugin.api.javascript;

import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.TwasiUserPlugin;

import javax.script.ScriptEngine;
import java.io.File;
import java.util.List;

public class JSPlugin<T> extends TwasiPlugin<T> {

    public JSPlugin(ScriptEngine pluginEnv, ScriptEngine userPluginEnv, List<ScriptEngine> commandEngines, List<ScriptEngine> variableEngines, JSPluginConfig config, File root) {

    }

    @Override
    public Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return null;
    }
}
