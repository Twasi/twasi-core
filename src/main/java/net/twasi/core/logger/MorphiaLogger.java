package net.twasi.core.logger;

import org.mongodb.morphia.logging.Logger;

public class MorphiaLogger implements Logger {
    @Override
    public void debug(String s) {
        TwasiLogger.log.debug(s);
    }

    @Override
    public void debug(String s, Object... objects) {
        TwasiLogger.log.debug(s);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        TwasiLogger.log.debug(s, throwable);
    }

    @Override
    public void error(String s) {
        TwasiLogger.log.error(s);
    }

    @Override
    public void error(String s, Object... objects) {
        TwasiLogger.log.error(s);
    }

    @Override
    public void error(String s, Throwable throwable) {
        TwasiLogger.log.error(s, throwable);
    }

    @Override
    public void info(String s) {
        TwasiLogger.log.info(s);
    }

    @Override
    public void info(String s, Object... objects) {
        TwasiLogger.log.info(s);
    }

    @Override
    public void info(String s, Throwable throwable) {
        TwasiLogger.log.info(s, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return TwasiLogger.log.isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return TwasiLogger.log.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return TwasiLogger.log.isTraceEnabled();
    }

    @Override
    public boolean isWarningEnabled() {
        return true;
    }

    @Override
    public void trace(String s) {
        TwasiLogger.log.trace(s);
    }

    @Override
    public void trace(String s, Object... objects) {
        TwasiLogger.log.trace(s);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        TwasiLogger.log.trace(s, throwable);
    }

    @Override
    public void warning(String s) {
        TwasiLogger.log.warn(s);
    }

    @Override
    public void warning(String s, Object... objects) {
        TwasiLogger.log.warn(s);
    }

    @Override
    public void warning(String s, Throwable throwable) {
        TwasiLogger.log.warn(s, throwable);
    }
}
