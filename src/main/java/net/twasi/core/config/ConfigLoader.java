package net.twasi.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.twasi.core.config.ConfigCatalog.ConfigCatalog;
import net.twasi.core.logger.TwasiLogger;
import org.apache.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

class ConfigLoader {

    ConfigCatalog configCatalog;

    ConfigLoader() {
        if (!new File("twasi.yml").exists()) {
            try{
                PrintWriter writer = new PrintWriter("twasi.yml", "UTF-8");
                writer.println("# This is the default Twasi configuration.\n");
                writer.println("# The default database connection");
                writer.println("database:");
                writer.println("  type: MongoDB # Only type that is supported");
                writer.println("  hostname: localhost");
                writer.println("  port: 27017");
                writer.println("  user: twasi");
                writer.println("  password: Ultr4S4f3p455w0rd");
                writer.println("  database: twasidb");
                writer.println("  port: 27017");
                writer.println("twitch:");
                writer.println("  hostname: irc.twitch.tv");
                writer.println("  port: 6667");
                writer.println("  defaultName: Twasibot");
                writer.println("  defaultToken: oauth:OAuthToken");
                writer.println("  defaultUserId: TWITCHID");
                writer.println("  clientId: TWITCH_CLIENTID");
                writer.println("  clientSecret: TWITCH_SECRET");
                writer.println("  redirectUri: http://localhost:8000/auth/callback");
                writer.println("log:");
                writer.println("  level: ALL");
                writer.println("auth:");
                writer.println("  issuer: Twasi");
                writer.println("webinterface:");
                writer.println("  port: 8000");
                writer.close();
                TwasiLogger.log.info("Default config file created.");
            } catch (IOException e) {
                TwasiLogger.log.error("Cannot write config file: " + e.getMessage());
            }
        }

        // Parse
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            configCatalog = mapper.readValue(new File("twasi.yml"), ConfigCatalog.class);
        } catch (Exception e) {
            TwasiLogger.log.error("Cannot parse config file: " + e.getMessage());
        }

        TwasiLogger.log.debug("Config file loaded");

        // Apply logger level
        TwasiLogger.setLogLevel(Level.toLevel(configCatalog.log.level));
        System.out.println("Loglevel set to " + configCatalog.log.level);
    }

}
