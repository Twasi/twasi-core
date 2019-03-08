package net.twasi.core.plugin;

import net.twasi.core.database.models.User;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.translations.TwasiTranslation;

public abstract class TwasiDependency extends TwasiPlugin {
    private TwasiTranslation twasiTranslation = new TwasiTranslation(getClassLoader());

    public Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return null;
    }

    @Override
    public boolean isDependency() {
        return true;
    }

    public String getTranslation(User user, String key, Object... objects) {
        return twasiTranslation.getTranslation(user, key, objects);
    }

    public String getRandomTranslation(User user, String key, Object... objects) {
        return twasiTranslation.getRandomTranslation(user, key, objects);
    }
}
