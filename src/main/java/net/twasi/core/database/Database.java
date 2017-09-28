package net.twasi.core.database;

import com.mongodb.MongoClient;
import net.twasi.core.config.Config;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class Database {

    public static Morphia morphia = new Morphia();
    private static Datastore store;

    public static void connect() {
        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("net.twasi.database.models");

        // create the Datastore connecting to the default port on the local host
        store = morphia.createDatastore(new MongoClient(), Config.catalog.database.database);
        store.ensureIndexes();
    }

    public static void disconnect() {
    }

    public static Datastore getStore() {
        return store;
    }

    public static Morphia getMorphia() { return morphia; }
}
