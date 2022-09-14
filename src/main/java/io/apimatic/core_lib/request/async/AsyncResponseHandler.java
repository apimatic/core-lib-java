package io.apimatic.core_lib.request.async;

import java.io.IOException;

import io.apimatic.core_interfaces.http.request.CoreHttpRequest;
import io.apimatic.core_interfaces.http.response.CoreHttpResponse;
import io.apimatic.core_lib.CoreConfig;
import io.apimatic.core_lib.types.ApiException;

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
    T handle(CoreHttpRequest httpRequest, CoreHttpResponse httpResponse, CoreConfig coreConfig)
            throws IOException, ExceptionType;
}
