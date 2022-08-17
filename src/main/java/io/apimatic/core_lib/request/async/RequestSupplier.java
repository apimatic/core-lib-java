package io.apimatic.core_lib.request.async;

import java.io.IOException;

import io.apimatic.core_interfaces.http.request.CoreHttpRequest;

/**
 * 
 * Request Supplier
 *
 */
@FunctionalInterface
public interface RequestSupplier {
    /**
     * Supplies the HttpRequest object.
     * 
     * @return An object of type HttpRequest
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    CoreHttpRequest supply() throws IOException;
}
