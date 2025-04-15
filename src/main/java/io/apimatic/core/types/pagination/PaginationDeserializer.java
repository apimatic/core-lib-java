package io.apimatic.core.types.pagination;

import java.io.IOException;

import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * Functional Interface to apply the deserializer function.
 * 
 * @param <T> The type of the response to deserialize into.
 */
@FunctionalInterface
public interface PaginationDeserializer {
    /**
     * Apply the deserialization function and returns the ResponseType response.
     * 
     * @param response The response of current API Call.
     * @param config   The EndPoint configuration for paginated API Calls.
     * @return The deserialized data.
     * @throws IOException Exception to be thrown while applying the function.
     */
    Object apply(Response response, EndpointConfiguration config) throws IOException;
}