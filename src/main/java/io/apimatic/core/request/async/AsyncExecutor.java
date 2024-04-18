package io.apimatic.core.request.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.logger.ApiLogger;

/**
 * Executor service for asynchronous HTTP end point call.
 */
public final class AsyncExecutor {

    /**
     * Private constructor to prevent instantiation
     */
    private AsyncExecutor() {
    }

    /**
     * Make an asynchronous HTTP end point call.
     * @param <ResponseType>  The type of the object for response.
     * @param <ExceptionType> Server error.
     * @param requestSupplier An object of RequestSupplier to supply an instance of
     *                        HttpRequest.
     * @param requestExecutor An object of RequestExecutor to execute the given
     *                        request.
     * @param responseHandler An object of ResponseHandler to handle the endpoint
     *                        response.
     * @param apiLogger       The logger to use.
     * @return An object of type CompletableFuture of T.
     */
    public static <ResponseType, ExceptionType extends CoreApiException>
        CompletableFuture<ResponseType> makeHttpCallAsync(
            RequestSupplier requestSupplier, RequestExecutor requestExecutor,
            AsyncResponseHandler<ResponseType, ExceptionType> responseHandler,
            ApiLogger apiLogger) {
        final Request request;
        try {
            request = requestSupplier.supply();
            apiLogger.logRequest(request);
        } catch (Exception e) {
            CompletableFuture<ResponseType> futureResponse = new CompletableFuture<>();
            futureResponse.completeExceptionally(e);
            return futureResponse;
        }

        // Invoke request and get response
        return requestExecutor.execute(request).thenApplyAsync(response -> {
            apiLogger.logResponse(response);
            try {
                return responseHandler.handle(request, response);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }
}
