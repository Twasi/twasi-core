package net.twasi.core.logger;

import org.mongodb.morphia.logging.Logger;
import org.mongodb.morphia.logging.LoggerFactory;

public class TwasiLoggerFactory implements LoggerFactory {
    private static MorphiaVoidLogger logger = new MorphiaVoidLogger();

    @Override
    public Logger get(Class<?> aClass) {
        return logger;
    }
}
