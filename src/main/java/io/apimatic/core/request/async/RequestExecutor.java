package io.apimatic.core.request.async;

import java.util.concurrent.CompletableFuture;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * A Request executor that executes request and returns the response asynchronously
 */
@FunctionalInterface
public interface RequestExecutor {

    /**
     * Execute a given Request to get the response back.
     * @param request The given Request to execute
     * @return CompletableFuture of Response after execution
     */
    CompletableFuture<Response> execute(Request request);
}
