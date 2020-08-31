package net.twasi.core.graphql;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.ResultPath;
import graphql.language.SourceLocation;
import net.twasi.core.logger.TwasiLogger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphQLExceptionHandler implements DataFetcherExceptionHandler {
    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();
        SourceLocation sourceLocation = handlerParameters.getField().getSingleField().getSourceLocation();
        ResultPath path = handlerParameters.getPath();
        TwasiLogger.log.debug("Data fetcher exception while resolving GraphQL request", exception);

        if (exception instanceof TwasiGraphQLHandledException) {
            TwasiGraphQLHandledException handledException = (TwasiGraphQLHandledException) exception;
            return DataFetcherExceptionHandlerResult.newResult(handledException.getGraphQLException(sourceLocation, path)).build();
        }

        TwasiLogger.log.debug("Data fetcher failed with unhandled exception.", exception);
        return DataFetcherExceptionHandlerResult.newResult(new GraphQLError() {
            @Override
            public String getMessage() {
                return "Unknown error occurred.";
            }

            @Override
            public List<SourceLocation> getLocations() {
                return Collections.singletonList(sourceLocation);
            }

            @Override
            public Map<String, Object> getExtensions() {
                HashMap<String, Object> values = new HashMap<>();
                values.put("localisedKey", "error.unknown");
                return values;
            }

            @Override
            public ErrorClassification getErrorType() {
                return null;
            }

            @Override
            public List<Object> getPath() {
                return Collections.singletonList(path);
            }
        }).build();
    }
}