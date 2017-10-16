package net.twasi.core.database;

import com.mongodb.MongoClient;
import net.twasi.core.config.Config;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Class establishes connection to the database
 */
public class Database {

    public static Morphia morphia = new Morphia();
    private static Datastore store;

    /**
     * Connect to the database and map Model-Classes
     */
    public static void connect() {
        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("net.twasi.database.models");

        // create the Datastore connecting to the default port on the local host
        store = morphia.createDatastore(new MongoClient(), Config.catalog.database.database);
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
