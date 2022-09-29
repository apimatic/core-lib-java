package io.apimatic.core.request.async;

import java.io.IOException;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * A Handler that handles the response asynchronously
 *
 * @param <T> A response that is determined
 * @param <ExceptionType> in case of a problem
 */
@FunctionalInterface
public interface AsyncResponseHandler<T, ExceptionType extends CoreApiException> {
    /**
     * Handles the response for an endpoint.
     * 
     * @param context HttpContext of the request and the received response
     * @return An object of type T .
     * @throws CoreApiException Represents error response from the server.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    T handle(Request httpRequest, Response httpResponse) throws IOException, ExceptionType;
}
