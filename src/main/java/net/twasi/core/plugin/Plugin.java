package net.twasi.core.plugin;

public class Plugin {

    static PluginLoader loader;

    public static void load() {
        loader = new PluginLoader();
    }

}
