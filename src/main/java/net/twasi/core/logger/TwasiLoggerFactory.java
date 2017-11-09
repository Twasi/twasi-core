package net.twasi.core.logger;

import org.mongodb.morphia.logging.Logger;
import org.mongodb.morphia.logging.LoggerFactory;

public class TwasiLoggerFactory implements LoggerFactory {
    private static MorphiaLogger logger = new MorphiaLogger();

    @Override
    public Logger get(Class<?> aClass) {
        return logger;
    }
}
