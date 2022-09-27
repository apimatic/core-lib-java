package io.apimatic.core_lib.request.async;

import java.io.IOException;

import io.apimatic.core_interfaces.http.request.Request;
import io.apimatic.core_interfaces.http.request.configuration.EndpointSetting;
import io.apimatic.core_interfaces.http.response.Response;
import io.apimatic.core_lib.GlobalConfiguration;
import io.apimatic.core_lib.configurations.http.request.EndpointConfiguration;
import io.apimatic.core_lib.types.ApiException;

/**
 * A Handler that handles the response asynchronously
 *
 * @param <T> A response that is determined
 * @param <ExceptionType> in case of a problem
 */
@FunctionalInterface
public interface AsyncResponseHandler<T, ExceptionType extends ApiException> {
    /**
     * Handles the response for an endpoint.
     * 
     * @param context HttpContext of the request and the received response
     * @return An object of type T .
     * @throws ApiException Represents error response from the server.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    T handle(Request httpRequest, Response httpResponse) throws IOException, ExceptionType;
}
