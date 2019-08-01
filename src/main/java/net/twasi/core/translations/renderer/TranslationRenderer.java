package net.twasi.core.translations.renderer;

import net.twasi.core.database.models.Language;
import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.models.Streamer;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import net.twasi.core.services.providers.config.catalog.ConfigCatalog;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class TranslationRenderer {

    private static String bindingNotFound = "undefined";
    private static String keyNotFound = "no_translation_key";
    private static String noTranslations = "no_translations";
    private static String null_value = "null";

    private ClassLoader loader;
    private Language language;

    private Map<String, String> bindings = new HashMap<>();
    private Map<String, Object> objectBindings = new HashMap<>();
    private Map<String, DynamicBindingInterface> dynamicBindings = new HashMap<>();

    private TranslationRenderer(@NotNull ClassLoader loader, Language language) {
        this.loader = loader;
        this.language = language;
        if (language != null) defaultBindings();
    }

    public static TranslationRenderer getInstance(TwasiUserPlugin plugin) {
        User user = plugin.getTwasiInterface().getStreamer().getUser();
        TranslationRenderer renderer = new TranslationRenderer(plugin.getCorePlugin().getClassLoader(), user.getConfig().getLanguage());
        return renderer
                .multiBindObject(user.getTwitchAccount(), "user", "streamer")
                .multiBindObject(plugin.getCorePlugin().getDescription(), "plugin", "p");
    }

    public static TranslationRenderer getInstance(TwasiDependency twasiDependency) {
        return new TranslationRenderer(twasiDependency.getClassLoader(), Language.EN_GB)
                .multiBindObject(twasiDependency.getDescription(), "dependency", "dep", "plugin", "p");
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

    public TranslationRenderer multiBind(String value, String... keys) {
        Arrays.stream(keys).forEach(key -> bind(key, value));
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

    public TranslationRenderer multiBindObject(Object object, String... keys) {
        Arrays.stream(keys).forEach(key -> bindObject(key, object));
        return this;
    }

    public TranslationRenderer bindDynamic(String key, DynamicBindingInterface resolver) {
        dynamicBindings.put(key.toLowerCase(), resolver);
        return this;
    }

    public TranslationRenderer multiBindDynamic(DynamicBindingInterface resolver, String... keys) {
        Arrays.stream(keys).forEach(key -> bindDynamic(key, resolver));
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
        TwasiLogger.log.debug("Unable to resolve translation key: '" + translationKey + "'");
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
        while (ofString.matches(".*[{][a-zA-Z0-9.\\s-_äöüÄÖÜß:]+[}].*")) {
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
        if (this.bindings.containsKey(key.toLowerCase())) // Prioritize string-bindings
            return this.bindings.get(key.toLowerCase()); // Return string-binding if existing

        if (this.dynamicBindings.containsKey(key.toLowerCase()))  // Prioritize dynamic-bindings too
            return this.dynamicBindings.get(key.toLowerCase()).resolve(); // return resolved value

        if (!key.contains(".")) { // If there is no string-binding and there are no sub-objects requested (no dots)
            if (!objectBindings.containsKey(key.toLowerCase()))
                return bindingNotFound; // If there is no object for this binding too return undefined
            return objectToString(objectBindings.get(key.toLowerCase()), "");
        }

        String[] arrayParts = key.split("\\."); // Get object and sub-objects as string[]
        List<String> parts = new ArrayList<>(Arrays.asList(arrayParts)); // Make string[] to List<String> (for sub-objects)
        parts.remove(0); // Remove base-object from sub-objects-list
        Object ob = objectBindings.get(arrayParts[0].toLowerCase()); // Get base-object

        String options = null;

        try {
            outerloop:
            for (String part : parts) { // Loop through sub-objects
                Class resolvingClass = ob.getClass(); // Get class of parent object

                if (part.contains(":")) {
                    options = part.split(":")[1];
                    part = part.split(":")[0];
                } else options = null;

                // Try resolve as field
                String finalPart = part;
                Field foundField = Arrays.stream(resolvingClass.getFields()).filter(f -> f.getName().equals(finalPart)).findFirst().orElse(null); // Search for matching fields
                if (foundField == null) // If there is no perfect name match
                    foundField = Arrays.stream(resolvingClass.getFields()).filter(f -> f.getName().equalsIgnoreCase(finalPart)).findFirst().orElse(null); // Search case insensitive
                if (foundField != null) { // If there is a case-insensitive match
                    ob = foundField.get(ob); // Set new parent object to the field's value
                    continue; // And search for it's sub-objects
                }

                // Try resolve as method
                for (String prefix : new String[]{"", "get", "is", "has"}) { // Find method by different method-prefixes
                    String method = prefix + part; // Add prefix to method name
                    Method found = Arrays.stream(resolvingClass.getMethods()).filter(m -> m.getName().equals(method)).findFirst().orElse(null); // Search for matching methods
                    if (found == null) // If there is no perfect name match
                        found = Arrays.stream(resolvingClass.getMethods()).filter(m -> m.getName().equalsIgnoreCase(method)).findFirst().orElse(null); // Search case insensitive
                    if (found != null) { // If there is a case-insensitive match
                        ob = found.invoke(ob); // Set new parent object to the methods's return value
                        continue outerloop; // And search for it's sub-objects
                    }
                }
                return bindingNotFound;
            }

            return objectToString(ob, options);
        } catch (Exception e) {
            return bindingNotFound;
        }
    }

    private String objectToString(Object ob, String options) {
        if (ob == null) return null_value;
        String value = ob.toString();

        if (ob instanceof List) {
            List list = (List) ob;
            if (list.size() > 0) {
                int index = -1;
                if (options != null && !options.equals("")) {
                    try {
                        index = Integer.parseInt(options);
                    } catch (Exception ignored) {
                    }
                }
                try {
                    if (index >= 0) ob = list.get(index);
                    else {
                        list = new ArrayList(list);
                        Collections.shuffle(list);
                        ob = list.get(0);
                    }
                } catch (Exception e) {
                    value = null_value;
                }
            } else {
                value = null_value;
            }
        }

        if (ob instanceof Float || ob instanceof Double) {
            float f = (float) ob;
            f = Math.round(f * 100) / 100f; // 1.2345... -> 1.23
            value = String.valueOf(f);
        }

        if (ob instanceof Calendar)
            ob = ((Calendar) ob).getTime(); // Switch Calendar with assigned Date

        if (ob instanceof Date) {
            Date date = (Date) ob;
            if (options == null) options = "datetime";
            if (options.equalsIgnoreCase("date")) {
                value = language.getDateFormat().format(date);
            } else if (options.equalsIgnoreCase("time")) {
                value = new SimpleDateFormat("HH:mm").format(date);
            } else {
                value = new SimpleDateFormat(language.getDateFormat().toPattern() + " HH:mm").format(date);
            }
        }

        if (ob instanceof Integer) {
            value = String.valueOf((int) ob);
        }

        if (ob instanceof Long) {
            value = String.valueOf((long) ob);
        }

        if (ob instanceof Streamer) {
            value = ((Streamer) ob).getUser().getTwitchAccount().getDisplayName();
        }

        if (ob instanceof User) {
            value = ((User) ob).getTwitchAccount().getDisplayName();
        }

        if (ob instanceof TwitchAccount) {
            value = ((TwitchAccount) ob).getDisplayName();
        }

        return value;
    }

    private void defaultBindings() {
        ConfigCatalog catalog = ServiceRegistry.get(ConfigService.class).getCatalog();
        bind("prefix", catalog.bot.prefix)
                .multiBind(language.getLanguageName(), "lang", "language")
                .multiBind(getClass().getPackage().getImplementationVersion(), "version", "ver")
                .multiBindDynamic(() -> new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()), "time", "clocktime")
                .bindDynamic("date", () -> language.getDateFormat().format(Calendar.getInstance().getTime()));
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

    public interface DynamicBindingInterface {
        String resolve();
    }
}
