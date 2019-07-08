package net.twasi.core.graphql;

import graphql.execution.AsyncExecutionStrategy;
import graphql.schema.GraphQLSchema;
import graphql.servlet.*;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.ApiSchemaManagementService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GraphQLEndpoint extends GraphQLHttpServlet {

    private GraphQLSchema schema = ServiceRegistry.get(ApiSchemaManagementService.class).getDefinitiveSchema();

    @Override
    protected GraphQLConfiguration getConfiguration() {
        return GraphQLConfiguration
                .with(schema)
                .with(GraphQLQueryInvoker.newBuilder()
                        .withExecutionStrategyProvider(new DefaultExecutionStrategyProvider(new AsyncExecutionStrategy(new GraphQLExceptionHandler())))
                        .build())
                .with(GraphQLObjectMapper.newBuilder()
                        .withGraphQLErrorHandler(list -> list)
                        .build())
                .build();
    }

    /* private static GraphQLSchema buildSchema() {
        try {
            return SchemaParser.newParser()
                    .file("schema.graphqls")
                    .resolvers(new Query())
                    .build()
                    .makeExecutableSchema();
        } catch (Throwable t) {
            TwasiLogger.log.error("Cannot initialize SchemaParser", t);
            return null;
        }
    }*/

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        super.doOptions(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        super.doGet(req, resp);
    }
}
