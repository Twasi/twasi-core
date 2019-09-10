package net.twasi.core.plugin.api.javascript;

import net.twasi.core.database.models.Language;
import net.twasi.core.database.models.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class JSPluginTranslation extends net.twasi.core.translations.TwasiTranslation {

    public JSPluginTranslation() {
        super(null);
    }

    public JSPluginTranslation(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public String getTranslation(Language language, String translationKey) {
        throw new NotImplementedException();
    }

    @Override
    public String getRandomTranslation(Language language, String translationKey) {
        throw new NotImplementedException();
    }

    @Override
    public String getTranslation(User user, String translationKey) {
        throw new NotImplementedException();
    }

    @Override
    public String getTranslation(User user, String translationKey, Object... objects) {
        throw new NotImplementedException();
    }

    @Override
    public String getRandomTranslation(User user, String translationKey) {
        throw new NotImplementedException();
    }

    @Override
    public String getRandomTranslation(User user, String translationKey, Object... objects) {
        throw new NotImplementedException();
    }
}
