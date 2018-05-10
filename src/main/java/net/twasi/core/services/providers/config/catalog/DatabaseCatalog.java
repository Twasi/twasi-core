package net.twasi.core.services.providers.config.catalog;

public class DatabaseCatalog {
    public String type;
    public String hostname;
    public int port;
    public String database;
    public Authentication authentication;

    public class Authentication {
        public String user;
        public String password;
    }
}
