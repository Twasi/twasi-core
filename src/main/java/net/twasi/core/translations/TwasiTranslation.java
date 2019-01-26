package net.twasi.core.translations;

import net.twasi.core.database.models.Language;
import net.twasi.core.database.models.User;
import net.twasi.core.logger.TwasiLogger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class TwasiTranslation {
    private ClassLoader classLoader;

    public TwasiTranslation(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getTranslation(Language language, String translationKey) {
        return getTranslation(language, translationKey, false);
    }

    public String getRandomTranslation(Language language, String translationKey) {
        return getTranslation(language, translationKey, true);
    }

    private String getTranslation(Language language, String translationKey, boolean random) {

        if (language == null) {
            language = Language.EN_GB;
        }

        InputStream inputStream = classLoader.getResourceAsStream("translations/" + language.toString() + ".lang");

        if (inputStream == null && language == Language.EN_GB) {
            TwasiLogger.log.error("Default English language not found in classLoader resources folder. Please create a folder 'translations' with the file 'EN_GB.lang' in it.");
            return translationKey;
        }

        if (inputStream == null) {
            return getTranslation(Language.EN_GB, translationKey);
        }

        String result = random ? readRandomTranslation(inputStream, translationKey) : readTranslation(inputStream, translationKey);
        if (result != null) return result;

        if (language == Language.EN_GB) {
            TwasiLogger.log.warn("Unknown translation key '" + translationKey + "' searched, but not found. This may lead to unexpected results.");
            return translationKey;
        }

        // Default to english if nothing was found
        return getTranslation(Language.EN_GB, translationKey);
    }

    private String readTranslation(InputStream inputStream, String translationKey) {
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(translationKey + "=")) {
                    return line.substring(translationKey.length() + 1);
                }
            }
        } catch (Exception e) {
            TwasiLogger.log.error(e);
        }
        return null;
    }

    private String readRandomTranslation(InputStream inputStream, String translationKey) {
        ArrayList<String> matching = new ArrayList<>();
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(translationKey + "=")) {
                    matching.add(line.substring(translationKey.length() + 1));
                }
            }
            if (matching.size() > 0) {
                Collections.shuffle(matching);
                return matching.get(0);
            }
        } catch (Exception e) {
            TwasiLogger.log.error(e);
        }
        return null;
    }

    public String getTranslation(User user, String translationKey) {
        return getTranslation(user.getConfig().getLanguage(), translationKey);
    }

    public String getTranslation(User user, String translationKey, Object... objects) {
        return String.format(getTranslation(user, translationKey), objects);
    }

    public String getRandomTranslation(User user, String translationKey) {
        return getRandomTranslation(user.getConfig().getLanguage(), translationKey);
    }

    public String getRandomTranslation(User user, String translationKey, Object... objects) {
        return String.format(getRandomTranslation(user, translationKey), objects);
    }
}
