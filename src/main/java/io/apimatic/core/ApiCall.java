package io.apimatic.core;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.logger.SdkLoggerFactory;
import io.apimatic.core.request.async.AsyncExecutor;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.core.types.pagination.PageWrapper;
import io.apimatic.core.types.pagination.PaginatedData;
import io.apimatic.core.types.pagination.PaginationStrategy;
import io.apimatic.coreinterfaces.http.Context;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.ApiLogger;

/**
 * An API call, or API request, is a message sent to a server asking an API to provide a service or
 * information.
 * @param <ResponseType> resource from server.
 * @param <ExceptionType> in case of a problem or the connection was aborted.
 */
public final class ApiCall<ResponseType, ExceptionType extends CoreApiException> {

    /**
     * An instance of {@link GlobalConfiguration}.
     */
    private final GlobalConfiguration globalConfig;

    /**
     * An instance of {@link HttpRequest.Builder}
     */
    private final HttpRequest.Builder requestBuilder;

    /**
     * An instance of {@link ResponseHandler.Builder}.
     */
    private final ResponseHandler<ResponseType, ExceptionType> responseHandler;

    /**
     * An instance of {@link EndpointConfiguration}.
     */
    private final EndpointConfiguration endpointConfiguration;

    /**
     * An instance of {@link ApiLogger} for logging.
     */
    private final ApiLogger apiLogger;
    
    private Response response;


    /**
     * ApiCall constructor.
     * @param globalConfig the required configuration to built the ApiCall.
     * @param endpointConfiguration The endPoint configuration.
     * @param requestBuilder Http request builder for the api call.
     * @param responseHandler the handler for the response.
     */
    private ApiCall(final GlobalConfiguration globalConfig,
            final EndpointConfiguration endpointConfiguration,
            final HttpRequest.Builder requestBuilder,
            final ResponseHandler<ResponseType, ExceptionType> responseHandler) {
        this.globalConfig = globalConfig;
        this.requestBuilder = requestBuilder;
        this.responseHandler = responseHandler;
        this.endpointConfiguration = endpointConfiguration;
        this.apiLogger = SdkLoggerFactory.getLogger(globalConfig.getLoggingConfiguration());
    }
    
    public <T, I, P> T paginate(
            Function<PaginatedData<I, P, ResponseType, ExceptionType>, T> converter,
            Function<PageWrapper<I, ResponseType>, P> responseToPage,
            Function<ResponseType, List<I>> responseToItems,
            PaginationStrategy... strategies) {
        return converter.apply(new PaginatedData<I, P, ResponseType, ExceptionType>(
                this, responseToPage, responseToItems, strategies
        ));
    }
    
    public Response getResponse() {
        return response;
    }
    
    public HttpRequest.Builder getRequestBuilder() {
        return requestBuilder.copy();
    }

    /**
     * Execute the ApiCall and returns the expected response.
     * @return instance of ResponseType.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws ExceptionType Represents error response from the server.
     */
    public ResponseType execute() throws IOException, ExceptionType {
        Request request = requestBuilder.build(globalConfig);
        apiLogger.logRequest(request);
        response = globalConfig.getHttpClient().execute(request, endpointConfiguration);
        apiLogger.logResponse(response);

        Context context = globalConfig.getCompatibilityFactory()
                .createHttpContext(request, response);

        return responseHandler.handle(context, endpointConfiguration, globalConfig, requestBuilder);
    }

    /**
     * Execute the Api call asynchronously and returns the expected response in CompletableFuture.
     * @return the instance of {@link CompletableFuture}.
     */
    public CompletableFuture<ResponseType> executeAsync() {
        return AsyncExecutor.makeHttpCallAsync(() -> requestBuilder.build(globalConfig),
                request -> globalConfig.getHttpClient()
                        .executeAsync(request, endpointConfiguration),
                (request, response) -> {
                    this.response = response;
                    Context context = globalConfig.getCompatibilityFactory()
                            .createHttpContext(request, response);
                    return responseHandler.handle(context, endpointConfiguration, globalConfig,
                            requestBuilder);
                }, apiLogger);
    }
    
    public Builder<ResponseType, ExceptionType> toBuilder() {
        Builder<ResponseType, ExceptionType> builder = new Builder<ResponseType, ExceptionType>();
        
        builder.globalConfig = globalConfig;
        builder.endpointConfigurationBuilder = endpointConfiguration.toBuilder();
        builder.responseHandlerBuilder = responseHandler.toBuilder();
        builder.requestBuilder = requestBuilder.copy();
        
        return builder;
    }

    /**
     * Builder class for the {@link ApiCall} class.
     * @param <ResponseType> resource from server.
     * @param <ExceptionType> Represents error response from the server.
     */
    public static class Builder<ResponseType, ExceptionType extends CoreApiException> {
        /**
         * An instance of {@link GlobalConfiguration}.
         */
        private GlobalConfiguration globalConfig;

        /**
         * An instance of {@link HttpRequest.Builder}.
         */
        private HttpRequest.Builder requestBuilder = new HttpRequest.Builder();

        /**
         * An instance of {@link ResponseHandler.Builder}.
         */
        private ResponseHandler.Builder<ResponseType, ExceptionType> responseHandlerBuilder =
                new ResponseHandler.Builder<ResponseType, ExceptionType>();

        /**
         * An instance of {@link EndpointConfiguration.Builder}.
         */
        private EndpointConfiguration.Builder endpointConfigurationBuilder =
                new EndpointConfiguration.Builder();

        /**
         * @param globalConfig the configuration of Http Request.
         * @return {@link ApiCall.Builder}.
         */
        public Builder<ResponseType, ExceptionType> globalConfig(GlobalConfiguration globalConfig) {
            this.globalConfig = globalConfig;
            return this;
        }

        /**
         * @param action requestBuilder {@link Consumer}.
         * @return {@link ApiCall.Builder}.
         */
        public Builder<ResponseType, ExceptionType> requestBuilder(
                Consumer<HttpRequest.Builder> action) {
            requestBuilder = new HttpRequest.Builder();
            action.accept(requestBuilder);
            return this;
        }

        /**
         * @param builder requestBuilder {@link HttpRequest.Builder}.
         * @return {@link ApiCall.Builder}.
         */
        public Builder<ResponseType, ExceptionType> requestBuilder(
                HttpRequest.Builder builder) {
            requestBuilder = builder;
            return this;
        }

        /**
         * @param action responseHandler {@link Consumer}.
         * @return {@link ApiCall.Builder}.
         */
        public Builder<ResponseType, ExceptionType> responseHandler(
                Consumer<ResponseHandler.Builder<ResponseType, ExceptionType>> action) {
            responseHandlerBuilder = new ResponseHandler.Builder<ResponseType, ExceptionType>();
            action.accept(responseHandlerBuilder);
            return this;
        }

        /**
         * @param action endpointConfiguration {@link Consumer}.
         * @return {@link ApiCall.Builder}.
         */
        public Builder<ResponseType, ExceptionType> endpointConfiguration(
                Consumer<EndpointConfiguration.Builder> action) {
            endpointConfigurationBuilder = new EndpointConfiguration.Builder();
            action.accept(endpointConfigurationBuilder);
            return this;
        }

        /**
         * build the {@link ApiCall}.
         * @return the instance of {@link ApiCall}.
         */
        public ApiCall<ResponseType, ExceptionType> build() {
            return new ApiCall<ResponseType, ExceptionType>(globalConfig,
                    endpointConfigurationBuilder.build(), requestBuilder,
                    responseHandlerBuilder.build());
        }
    }
}
