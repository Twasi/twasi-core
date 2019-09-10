package net.twasi.core.plugin.api.javascript;

import net.twasi.core.interfaces.api.TwasiInterface;
import net.twasi.core.models.Message.TwasiMessage;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;

import javax.script.ScriptEngine;
import java.util.Collections;
import java.util.List;

import static net.twasi.core.plugin.api.javascript.JSPluginLoader.getObject;
import static net.twasi.core.plugin.api.javascript.JSPluginLoader.invokeObj;

public class JSPluginVariable extends TwasiVariable {

    private final ScriptEngine env;

    public JSPluginVariable(TwasiUserPlugin owner, ScriptEngine env) {
        super(owner);
        this.env = env;
    }

    @Override
    public List<String> getNames() {
        return Collections.singletonList(getObject(env, "variableName"));
    }

    @Override
    public String process(String name, TwasiInterface inf, String[] params, TwasiMessage message) {
        return invokeObj(env, "variableProcess", name, inf, params, message);
    }
}
