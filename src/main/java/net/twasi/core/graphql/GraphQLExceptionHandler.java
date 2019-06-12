package net.twasi.core.graphql;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.ExecutionPath;
import graphql.language.SourceLocation;
import net.twasi.core.logger.TwasiLogger;

import java.util.Collections;
import java.util.List;

public class GraphQLExceptionHandler implements DataFetcherExceptionHandler {
    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();
        SourceLocation sourceLocation = handlerParameters.getField().getSingleField().getSourceLocation();
        ExecutionPath path = handlerParameters.getPath();
        TwasiLogger.log.debug("Data fetcher exception while resolving GraphQL request", exception);

        if (exception instanceof TwasiGraphQLHandledException) {
            TwasiGraphQLHandledException handledException = (TwasiGraphQLHandledException) exception;
            return DataFetcherExceptionHandlerResult.newResult(handledException.getGraphQLException(sourceLocation, path)).build();
        }

        TwasiLogger.log.error("Data fetcher failed with unhandled exception.", exception);
        return DataFetcherExceptionHandlerResult.newResult(new GraphQLError() {
            @Override
            public String getMessage() {
                return exception.getMessage();
            }

            @Override
            public List<SourceLocation> getLocations() {
                return Collections.singletonList(sourceLocation);
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