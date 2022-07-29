package io.apimatic.core_lib.request.async;

import java.io.IOException;

import io.apimatic.core_interfaces.http.request.HttpRequest;
import io.apimatic.core_lib.types.ApiException;


/**
 * RequestSupplier.
 */
@FunctionalInterface
public interface RequestSupplier {
    /**
     * Supplies the HttpRequest object.
     * @return    An object of type HttpRequest
     * @throws    ApiException    Represents error response from the server.
     * @throws    IOException    Signals that an I/O exception of some sort has occurred.
    */
    HttpRequest supply() throws ApiException, IOException;
}

