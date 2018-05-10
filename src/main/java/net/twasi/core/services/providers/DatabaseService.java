package net.twasi.core.services.providers;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import net.twasi.core.logger.TwasiLoggerFactory;
import net.twasi.core.services.IService;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.config.ConfigService;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService implements IService {

    private Morphia morphia;
    private Datastore store;

    public DatabaseService() {
    }

    /**
     * Connect to the database and map Model-Classes
     */
    public void connect() {
        // Apply custom logger
        MorphiaLoggerFactory.registerLogger(TwasiLoggerFactory.class);
        // Disable Mongo Debugging
        System.setProperty("DEBUG.MONGO", "false");

        morphia = new Morphia();
        morphia.mapPackage("net.twasi.database.models");

        // Connect with given credentials
        Morphia morphia = new Morphia();
        ServerAddress addr = new ServerAddress(ServiceRegistry.get(ConfigService.class).getCatalog().database.hostname, ServiceRegistry.get(ConfigService.class).getCatalog().database.port);
        MongoClient client;

        // Use authentication?
        if (ServiceRegistry.get(ConfigService.class).getCatalog().database.authentication != null &&
                ServiceRegistry.get(ConfigService.class).getCatalog().database.authentication.user != null &&
                ServiceRegistry.get(ConfigService.class).getCatalog().database.authentication.password != null) {
            List<MongoCredential> credentialsList = new ArrayList<>();
            MongoCredential credential = MongoCredential.createCredential(
                    ServiceRegistry.get(ConfigService.class).getCatalog().database.authentication.user, ServiceRegistry.get(ConfigService.class).getCatalog().database.database, ServiceRegistry.get(ConfigService.class).getCatalog().database.authentication.password.toCharArray());
            credentialsList.add(credential);
            client = new MongoClient(addr, credentialsList);
        } else {
            client = new MongoClient(addr);
        }

        // Create the store
        store = morphia.createDatastore(client, ServiceRegistry.get(ConfigService.class).getCatalog().database.database);
        store.ensureIndexes();
    }

    /**
     * Disconnects from the database
     */
    public void disconnect() {
        throw new NotImplementedException();
    }

    /**
     * @return datastore to perform database operations
     */
    public Datastore getStore() {
        return store;
    }

    /**
     * @return morpia to perform operations on morpia
     */
    public Morphia getMorphia() {
        return morphia;
    }

}
