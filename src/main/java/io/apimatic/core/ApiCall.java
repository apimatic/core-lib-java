package io.apimatic.core;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.request.async.AsyncExecutor;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.request.configuration.CoreEndpointConfiguration;
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
     * An instance of {@link Request}
     */
    private final Request request;

    /**
     * An instance of {@link ResponseHandler.Builder}.
     */
    private final ResponseHandler<ResponseType, ExceptionType> responseHandler;

    /**
     * An instance of {@link CoreEndpointConfiguration}.
     */
    private final CoreEndpointConfiguration endpointConfiguration;
    
    private final ApiLogger apiLogger;

    /**
     * ApiCall constructor.
     * @param globalConfig the required configuration to built the ApiCall.
     * @param coreHttpRequest Http request for the api call.
     * @param responseHandler the handler for the response.
     * @param coreEndpointConfiguration endPoint configuration.
     */
    private ApiCall(final GlobalConfiguration globalConfig, final Request coreHttpRequest,
            final ResponseHandler<ResponseType, ExceptionType> responseHandler,
            final CoreEndpointConfiguration coreEndpointConfiguration) {
        this.globalConfig = globalConfig;
        this.request = coreHttpRequest;
        this.responseHandler = responseHandler;
        this.endpointConfiguration = coreEndpointConfiguration;
        this.apiLogger = globalConfig.getApiLogger();
    }

    /**
     * Execute the ApiCall and returns the expected response.
     * @return instance of ResponseType.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     * @throws ExceptionType Represents error response from the server.
     */
    public ResponseType execute() throws IOException, ExceptionType {
        Response httpResponse = null;
        
    	apiLogger.startScope();
    	apiLogger.logRequest(request, request.getUrl(endpointConfiguration.getArraySerializationFormat()));
    	try {
    		httpResponse = globalConfig.getHttpClient().execute(request, endpointConfiguration);
    		apiLogger.logResponse(request, httpResponse);
    	}
    	catch(IOException ex) {
    		apiLogger.logRequestError(request, request.getUrl(endpointConfiguration.getArraySerializationFormat()), ex);
    	}
    	finally {
    		apiLogger.closeScope();
    	}
    	
        return responseHandler.handle(request, httpResponse, globalConfig, endpointConfiguration);
    }

    /**
     * Execute the Api call asynchronously and returns the expected response in CompletableFuture.
     * @return the instance of {@link CompletableFuture}.
     */
    public CompletableFuture<ResponseType> executeAsync() {
        return AsyncExecutor.makeHttpCallAsync(() -> request,
                request -> globalConfig.getHttpClient().executeAsync(request,
                        endpointConfiguration),
                (httpRequest, httpResponse) -> responseHandler.handle(httpRequest, httpResponse,
                        globalConfig, endpointConfiguration), apiLogger);
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
         * @throws IOException Signals that an I/O exception of some sort has occurred.
         */
        public ApiCall<ResponseType, ExceptionType> build() throws IOException {
            return new ApiCall<ResponseType, ExceptionType>(globalConfig,
                    requestBuilder.build(globalConfig), responseHandlerBuilder.build(),
                    endpointConfigurationBuilder.build());
        }
    }
}
