package io.apimatic.core_lib.request.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import io.apimatic.core_interfaces.http.request.CoreHttpRequest;
import io.apimatic.core_lib.CoreConfig;
import io.apimatic.core_lib.types.ApiException;


public class AsyncExecutor {

    private CoreConfig coreConfig;

    public AsyncExecutor(CoreConfig coreConfig) {
        this.coreConfig = coreConfig;
    }

    /**
     * Make an asynchronous HTTP endpoint call.
     * 
     * @param <ResponseType> The type of the object for response
     * @param requestSupplier An object of RequestSupplier to supply an instance of HttpRequest
     * @param requestExecutor An object of RequestExecutor to execute the given request
     * @param responseHandler An object of ResponseHandler to handle the endpoint response
     * @return An object of type CompletableFuture of T
     */
    public <ResponseType, ExceptionType extends ApiException> CompletableFuture<ResponseType> makeHttpCallAsync(
            RequestSupplier requestSupplier, RequestExecutor requestExecutor,
            AsyncResponseHandler<ResponseType, ExceptionType> responseHandler) {
        final CoreHttpRequest request;
        try {
            request = requestSupplier.supply();
        } catch (Exception e) {
            CompletableFuture<ResponseType> futureResponse = new CompletableFuture<>();
            futureResponse.completeExceptionally(e);
            return futureResponse;
        }

        // Invoke request and get response
        return requestExecutor.execute(request).thenApplyAsync(response -> {
            try {
                return responseHandler.handle(request, response, coreConfig);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }
}
