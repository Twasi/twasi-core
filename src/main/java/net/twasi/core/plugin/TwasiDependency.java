package net.twasi.core.plugin;

import net.twasi.core.database.models.User;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.TwasiVariable;
import net.twasi.core.translations.TwasiTranslation;
import net.twasi.core.translations.renderer.TranslationRenderer;

import java.util.ArrayList;
import java.util.List;

public abstract class TwasiDependency<T> extends TwasiPlugin<T> {

    private TwasiTranslation twasiTranslation = new TwasiTranslation(getClassLoader());

    public final Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return null;
    }

    @Override
    public final boolean isDependency() {
        return true;
    }

    @Deprecated
    public final String getTranslation(User user, String key, Object... objects) {
        return twasiTranslation.getTranslation(user, key, objects);
    }

    @Deprecated
    public final String getRandomTranslation(User user, String key, Object... objects) {
        return twasiTranslation.getRandomTranslation(user, key, objects);
    }

    public final TranslationRenderer getTranslationRenderer(){
        return TranslationRenderer.getInstance(this);
    }

    public List<TwasiVariable> getVariables() {
        return new ArrayList<>();
    }

}
