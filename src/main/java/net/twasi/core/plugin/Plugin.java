package net.twasi.core.plugin;

public class Plugin {

    private static PluginLoader loader;

    public static void load() {
        loader = new PluginLoader();
    }

}
