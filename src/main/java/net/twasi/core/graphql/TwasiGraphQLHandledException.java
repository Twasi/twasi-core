package net.twasi.core.graphql;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwasiGraphQLHandledException extends RuntimeException {
    private Map<String, Object> extensions = new HashMap<>();

    public TwasiGraphQLHandledException(String message, String localisedKey) {
        super(message);
        extensions.put("localisedKey", localisedKey);
    }

    public TwasiGraphQLHandledException putExtension(String name, Object value) {
        extensions.put(name, value);
        return this;
    }

    public GraphQLError getGraphQLException(SourceLocation location, ResultPath path) {
        return new GraphQLError() {
            public String getMessage() {
                return TwasiGraphQLHandledException.this.getMessage();
            }

            @Override
            public List<SourceLocation> getLocations() {
                return Collections.singletonList(location);
            }

            @Override
            public Map<String, Object> getExtensions() {
                return TwasiGraphQLHandledException.this.extensions;
            }

            @Override
            public ErrorClassification getErrorType() {
                return null;
            }

            @Override
            public List<Object> getPath() {
                return path.toList();
            }
        };
    }

}
