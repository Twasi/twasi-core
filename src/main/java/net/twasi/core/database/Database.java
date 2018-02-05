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
import org.mongodb.morphia.mapping.DefaultCreator;
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
        // MorphiaLoggerFactory.registerLogger(TwasiLoggerFactory.class);
        // Disable Mongo Debugging
        System.setProperty("DEBUG.MONGO", "false");

        morphia = new Morphia();

        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("net.twasi.database.models");

        // Connect with given credentials
        Morphia morphia = new Morphia();
        ServerAddress addr = new ServerAddress(Config.getCatalog().database.hostname, Config.getCatalog().database.port);
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        MongoCredential credentia = MongoCredential.createCredential(
                Config.getCatalog().database.user, Config.getCatalog().database.database, Config.getCatalog().database.password.toCharArray());
        credentialsList.add(credentia);
        MongoClient client = new MongoClient(addr, credentialsList);

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
    public static Morphia getMorphia() { return morphia; }
}
