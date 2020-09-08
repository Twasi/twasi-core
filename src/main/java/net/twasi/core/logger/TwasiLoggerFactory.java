package net.twasi.core.logger;

import dev.morphia.logging.Logger;
import dev.morphia.logging.LoggerFactory;

public class TwasiLoggerFactory implements LoggerFactory {
    private static MorphiaVoidLogger logger = new MorphiaVoidLogger();

    @Override
    public Logger get(Class<?> aClass) {
        return logger;
    }
}
