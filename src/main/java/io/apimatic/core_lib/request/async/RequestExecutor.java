package io.apimatic.core_lib.request.async;

import java.util.concurrent.CompletableFuture;

import io.apimatic.core_interfaces.http.request.Request;
import io.apimatic.core_interfaces.http.response.Response;

@FunctionalInterface
public interface RequestExecutor {

    /**
     * Execute a given HttpRequest to get the response back.
     * 
     * @param request The given HttpRequest to execute
     * @return CompletableFuture of HttpResponse after execution
     */
    CompletableFuture<Response> execute(Request request);
}
