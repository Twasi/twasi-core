package net.twasi.core.plugin;

public abstract class PluginBase implements Plugin {
    @Override
    public final int hashCode() {
        return getName().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return this == obj || obj != null && obj instanceof Plugin && getName().equals(((Plugin) obj).getName());
    }

    public final String getName() {
        return getDescription().getName();
    }
}
