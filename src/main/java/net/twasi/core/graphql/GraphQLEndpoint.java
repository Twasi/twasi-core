package net.twasi.core.graphql;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import net.twasi.core.graphql.repository.UserRepository;
import net.twasi.core.logger.TwasiLogger;

public class GraphQLEndpoint extends SimpleGraphQLServlet {

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        UserRepository userRepository = new UserRepository();
        try {
            return SchemaParser.newParser()
                    .file("schema.graphqls")
                    .resolvers(new Query(userRepository))
                    .build()
                    .makeExecutableSchema();
        } catch (Throwable t) {
            TwasiLogger.log.error("Cannot initialize SchemaParser", t);
            return null;
        }
    }

}
