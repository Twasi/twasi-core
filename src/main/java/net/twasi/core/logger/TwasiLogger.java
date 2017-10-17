package net.twasi.core.logger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class TwasiLogger {

    public final static Logger log = Logger.getLogger(Logger.class);

    public static void setLogLevel(Level level) {
        log.setLevel(level);
    }
}
