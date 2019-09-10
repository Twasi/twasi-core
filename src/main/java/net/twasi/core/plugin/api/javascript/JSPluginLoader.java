package net.twasi.core.plugin.api.javascript;

import com.google.gson.Gson;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.TwasiPluginConfiguration;

import javax.script.ScriptEngine;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JSPluginLoader {

    public static TwasiPlugin<?> loadPlugin(File root) throws Exception {
        File main = getChild(root, "plugin.js", false);
        assert main != null;

        ScriptEngine env = getExecutionEnvironment(main);

        Object obj = getObject(env, "config");
        JSPluginConfig config = mapObject(obj, JSPluginConfig.class);

        File commands = getChild(root, config.commandsFolder, true);
        File variables = getChild(root, config.variablesFolder, true);

        ScriptEngine pluginEnv = getExecutionEnvironment(getChild(root, config.pluginFile, false));
        ScriptEngine userPluginEnv = getExecutionEnvironment(getChild(root, config.userPluginFile, false));

        // TODO Check null and log

        List<ScriptEngine> commandEngines = new ArrayList<>();
        List<ScriptEngine> variableEngines = new ArrayList<>();

        if (commands != null) {
            for (File file : commands.listFiles(f -> f.getName().endsWith(".js"))) {
                commandEngines.add(getExecutionEnvironment(file));
            }
        }

        if (variables != null) {
            for (File file : variables.listFiles(f -> f.getName().endsWith(".js"))) {
                variableEngines.add(getExecutionEnvironment(file));
            }
        }

        return new JSPlugin<TwasiPluginConfiguration>(pluginEnv, userPluginEnv, commandEngines, variableEngines, config, root);
    }

    private static File getChild(File parent, String name, boolean folder) {
        File[] files = parent.listFiles(f -> f.getName().equalsIgnoreCase(name) && ((!folder && !f.isDirectory()) || folder && f.isDirectory()));
        if (files == null || files.length == 0) return null;
        else return files[0];
    }

    public static ScriptEngine getExecutionEnvironment(File script) {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine service = factory.getScriptEngine("--language=es6");

        try {
            service.eval("load('classpath:jvm-npm.js');");
            service.eval("var global = {};");
            service.eval("var plugin = {};");
            service.eval(new FileReader(script));
            return service;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static void invoke(ScriptEngine env, String function, Object... params) {
        try {
            ScriptObjectMirror obj = (ScriptObjectMirror) env.get("plugin");
            obj.callMember(function, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static <T> T invokeObj(ScriptEngine env, String function, Object... params) {
        try {
            ScriptObjectMirror obj = (ScriptObjectMirror) env.get("plugin");
            return (T) obj.callMember(function, params);
        } catch (Exception e) {
            return null;
        }
    }

    static <T> T getObject(ScriptEngine env, String name) {
        try {
            ScriptObjectMirror obj = (ScriptObjectMirror) env.get("plugin");
            return (T) obj.getMember(name);
        } catch (Exception e) {
            return null;
        }
    }

    static <T> T mapObject(Object source, final Class<T> dataClass) {
        return new Gson().fromJson(new Gson().toJson(source), dataClass);
    }

}
