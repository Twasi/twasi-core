package net.twasi.core.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import net.twasi.core.config.Config;
import net.twasi.core.database.store.UserStore;
import net.twasi.core.logger.TwasiLoggerFactory;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class establishes connection to the database
 */
public class Database {

    private static Morphia morphia;
    private static Datastore store;

    /**
     * Connect to the database and map Model-Classes
     */
    public static void connect() {
        // Apply custom logger
        MorphiaLoggerFactory.registerLogger(TwasiLoggerFactory.class);
        // Disable Mongo Debugging
        System.setProperty("DEBUG.MONGO", "false");

        morphia = new Morphia();
        morphia.mapPackage("net.twasi.database.models");

        // Connect with given credentials
        Morphia morphia = new Morphia();
        ServerAddress addr = new ServerAddress(Config.getCatalog().database.hostname, Config.getCatalog().database.port);
        MongoClient client;

        // Use authentication?
        if (Config.getCatalog().database.authentication != null &&
                Config.getCatalog().database.authentication.user != null &&
                Config.getCatalog().database.authentication.password != null) {
            List<MongoCredential> credentialsList = new ArrayList<>();
            MongoCredential credential = MongoCredential.createCredential(
                    Config.getCatalog().database.authentication.user, Config.getCatalog().database.database, Config.getCatalog().database.authentication.password.toCharArray());
            credentialsList.add(credential);
            client = new MongoClient(addr, credentialsList);
        } else {
            client = new MongoClient(addr);
        }

        // Create the store
        store = morphia.createDatastore(client, Config.getCatalog().database.database);
        store.ensureIndexes();

        UserStore.loadUsers();
    }

    /**
     * Disconnects from the database
     */
    public static void disconnect() {
        throw new NotImplementedException();
    }

    /**
     * @return datastore to perform database operations
     */
    public static Datastore getStore() {
        return store;
    }

    /**
     * @return morpia to perform operations on morpia
     */
    public static Morphia getMorphia() {
        return morphia;
    }
}
