package net.twasi.core.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import net.twasi.core.config.Config;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

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

        // Connect with given credentials
        Morphia morphia = new Morphia();
        ServerAddress addr = new ServerAddress(Config.catalog.database.hostname, Config.catalog.database.port);
        List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
        MongoCredential credentia = MongoCredential.createCredential(
                Config.catalog.database.user, Config.catalog.database.database, Config.catalog.database.password.toCharArray());
        credentialsList.add(credentia);
        MongoClient client = new MongoClient(addr, credentialsList);

        // Create the store
        store = morphia.createDatastore(client, Config.catalog.database.database);
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
