package net.twasi.core.translations.renderer;

import net.twasi.core.database.models.Language;
import net.twasi.core.database.models.User;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.plugin.api.TwasiUserPlugin;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class TranslationRenderer {

    private static String bindingNotFound = "unknown_binding".toUpperCase();
    private static String keyNotFound = "unknown_translation_key".toUpperCase();
    private static String noTranslations = "no_translations_provided".toUpperCase();

    private ClassLoader loader;
    private Language language;

    private Map<String, String> bindings = new HashMap<>();
    private Map<String, Object> objectBindings = new HashMap<>();

    private TranslationRenderer(ClassLoader loader, Language language) {
        this.loader = loader;
        this.language = language;
    }

    public static TranslationRenderer getInstance(TwasiUserPlugin plugin) {
        User user = plugin.getTwasiInterface().getStreamer().getUser();
        TranslationRenderer renderer = new TranslationRenderer(plugin.getCorePlugin().getClassLoader(), user.getConfig().getLanguage());
        return renderer
                .bindObject("user", user.getTwitchAccount());
    }

    public static TranslationRenderer getInstance(TwasiDependency twasiDependency) {
        return new TranslationRenderer(twasiDependency.getClassLoader(), Language.EN_GB);
    }

    public static TranslationRenderer getInstance(ClassLoader loader, Language language) {
        return new TranslationRenderer(loader, language);
    }

    public TranslationRenderer bind(String key, String value) {
        bindings.put(key.toLowerCase(), value);
        return this;
    }

    public TranslationRenderer bindAll(Map<String, String> bindings) {
        bindings.forEach(this::bind);
        return this;
    }

    public TranslationRenderer bindObject(String key, Object object) {
        if (key.contains(".")) throw new RuntimeException("Object binding key cannot contain dots.");
        objectBindings.put(key.toLowerCase(), object);
        return this;
    }

    public TranslationRenderer bindAllObjects(Map<String, Object> objectBindings) {
        objectBindings.forEach(this::bindObject);
        return this;
    }

    private InputStream getInputStream(Language language) {
        InputStream inputStream = loader.getResourceAsStream("translations/" + language.toString() + ".lang");
        if (inputStream == null && language != Language.EN_GB)
            inputStream = loader.getResourceAsStream("translations/" + Language.EN_GB.toString() + ".lang");
        return inputStream;
    }

    public String render(String translationKey, Language language) {
        InputStream inputStream = getInputStream(language);
        if (inputStream == null) return noTranslations;
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith(translationKey + "=")) {
                return resolveBindings(line.substring(translationKey.length() + 1));
            }
        }
        return keyNotFound;
    }

    public String render(String translationKey) {
        return render(translationKey, this.language);
    }

    public String renderRandom(String translationKey, Language language) {
        InputStream inputStream = getInputStream(language);
        List<String> matches = new ArrayList<>();
        if (inputStream == null) return noTranslations;
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith(translationKey + "=")) {
                matches.add(line.substring(translationKey.length() + 1));
            }
        }
        if (matches.size() > 0) {
            Collections.shuffle(matches);
            return resolveBindings(matches.get(0));
        }
        return keyNotFound;
    }

    public String renderRandom(String translationKey) {
        return renderRandom(translationKey, this.language);
    }

    private String resolveBindings(String ofString) {
        while (ofString.matches(".*[{][a-zA-Z0-9.\\s-_äöüÄÖÜß]+[}].*")) {
            int s_index = 0, e_index = 0;
            int counter = 0;
            for (char c : ofString.toCharArray()) {
                counter++;
                if (c == '{') s_index = counter - 1;
                if (c == '}') {
                    e_index = counter;
                    break;
                }
            }
            String rawElement = ofString.substring(s_index, e_index),
                    key = rawElement
                            .replaceFirst("\\s*\\{", "")
                            .replaceAll("}\\s*$", "")
                            .replaceAll("^\\s*", "")
                            .replaceAll("\\s+$", "")
                            .trim();
            ofString = ofString.replaceAll(Pattern.quote(rawElement), resolveBinding(key));
        }
        return ofString;
    }

    private String resolveBinding(String key) {
        if (this.bindings.containsKey(key.toLowerCase()))
            return this.bindings.get(key.toLowerCase());
        if (!key.contains(".")) return bindingNotFound;
        String[] arrayParts = key.split("\\.");
        List<String> parts = new ArrayList<>(Arrays.asList(arrayParts));
        parts.remove(0);
        Object ob = objectBindings.get(arrayParts[0].toLowerCase());
        try {
            outerloop:
            for (String part : parts) {
                Class resolvingClass = ob.getClass();
                // Try resolve as field
                Field foundField = Arrays.stream(resolvingClass.getFields()).filter(f -> f.getName().equals(part)).findFirst().orElse(null);
                if (foundField == null)
                    foundField = Arrays.stream(resolvingClass.getFields()).filter(f -> f.getName().equalsIgnoreCase(part)).findFirst().orElse(null);
                if (foundField != null) {
                    ob = foundField.get(ob);
                    continue;
                }
                // Try resolve as method
                for (String method : new String[]{part, "get" + part}) {
                    Method found = Arrays.stream(resolvingClass.getMethods()).filter(m -> m.getName().equals(method)).findFirst().orElse(null);
                    if (found == null)
                        found = Arrays.stream(resolvingClass.getMethods()).filter(m -> m.getName().equalsIgnoreCase(method)).findFirst().orElse(null);
                    if (found != null) {
                        ob = found.invoke(ob);
                        continue outerloop;
                    }
                }
                return bindingNotFound;
            }
            return ob.toString();
        } catch (Exception e) {
            return bindingNotFound;
        }
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public Language getLanguage() {
        return language;
    }

    public Map<String, String> getBindings() {
        return bindings;
    }

    public Map<String, Object> getObjectBindings() {
        return objectBindings;
    }
}
