package net.twasi.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.twasi.core.config.ConfigCatalog.ConfigCatalog;
import net.twasi.core.logger.TwasiLogger;

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
                writer.println("  user: root");
                writer.println("  password: Ultr4S4f3p455w0rd");
                writer.println("  database: twasidb");
                writer.println("twitch:");
                writer.println("  hostname: irc.twitch.tv");
                writer.println("  port: 6667");
                writer.println("  defaultName: Twasibot");
                writer.println("  defaultToken: oauth:OAuthToken");
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

        TwasiLogger.log.info("Config file loaded");
    }

}
