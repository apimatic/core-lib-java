package io.apimatic.core.types.pagination;

import java.io.IOException;

import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.HttpRequest;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * Functional Interface to apply the deserializer function.
 */
@FunctionalInterface
public interface PaginationDeserializer {
    /**
     * Apply the deserialization function and returns the ResponseType response.
     *
     * @param config The EndPoint configuration for paginated API Calls.
     * @param globalConfig The EndPoint configuration for paginated API Calls.
     * @param requestBuilder The requestBuilder to re create current API Call.
     * @param response The response of current API Call.
     *
     * @return The deserialized data.
     * @throws IOException Exception to be thrown while applying the function.
     */
    Object apply(EndpointConfiguration config, GlobalConfiguration globalConfig,
            HttpRequest.Builder requestBuilder, Response response) throws IOException;
}
