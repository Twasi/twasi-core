package net.twasi.core.services.providers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.SchemaParser;
import com.coxautodev.graphql.tools.SchemaParserBuilder;
import com.google.common.io.Resources;
import graphql.schema.GraphQLSchema;
import kotlin.text.Charsets;
import net.twasi.core.graphql.Query;
import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.services.IService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ApiSchemaManagementService implements IService {
    private GraphQLSchema definitiveSchema;
    private SchemaParserBuilder schemaBuilder = SchemaParser.newParser()
            .resolvers(new Query());

    private List<String> pluginNames = new ArrayList<>();

    public void addForPlugin(String pluginName, String definition, GraphQLQueryResolver resolver) {
        schemaBuilder.schemaString(definition)
                .resolvers(resolver);
        pluginNames.add(pluginName);
    }

    public GraphQLSchema getDefinitiveSchema() {
        return definitiveSchema;
    }

    public void executeBuild() {
        try {
            String defaultSchemaString = Resources.toString(Resources.getResource("schema.graphqls"), Charsets.UTF_8);

            String pluginDefinitions = pluginNames
                    .stream()
                    .map(pluginName -> ",\n    " + pluginName.toLowerCase() + "(token: String): " + pluginName)
                    .collect(Collectors.joining(","));

            defaultSchemaString = defaultSchemaString.replace("%PLUGINS%", pluginDefinitions);

            TwasiLogger.log.debug("Plugin API definitions: " + pluginDefinitions);

            schemaBuilder.schemaString(defaultSchemaString);

            definitiveSchema = schemaBuilder.build().makeExecutableSchema();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SchemaParserBuilder getSchemaBuilder() {
        if (definitiveSchema != null) {
            TwasiLogger.log.error("Tried to get schema builder after definitve schema was built. The schema can't be changed.");
            return null;
        }
        return schemaBuilder;
    }
}
