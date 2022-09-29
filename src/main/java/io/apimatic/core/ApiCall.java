package io.apimatic.core;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import io.apimatic.core.configurations.http.request.EndpointConfiguration;
import io.apimatic.core.request.async.AsyncExecutor;
import io.apimatic.core.types.CoreApiException;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.request.configuration.EndpointSetting;
import io.apimatic.coreinterfaces.http.response.Response;

/**
 * An API call, or API request, is a message sent to a server asking an API to provide a service or
 * information
 *
 * @param <ResponseType> resource from server
 * @param <ExceptionType> in case of a problem or the connection was aborted
 */
public class ApiCall<ResponseType, ExceptionType extends CoreApiException> {

    private final GlobalConfiguration globalConfig;
    private final Request request;
    private final ResponseHandler<ResponseType, ExceptionType> responseHandler;
    private final EndpointSetting endpointConfiguration;

    /**
     * ApiCall constructor
     * 
     * @param globalConfig the required configuration to built the ApiCall
     * @param coreHttpRequest Http request for the api call
     * @param responseHandler the handler for the response
     * @param coreEndpointConfiguration endPoint configuration
     */
    private ApiCall(GlobalConfiguration globalConfig, Request coreHttpRequest,
            ResponseHandler<ResponseType, ExceptionType> responseHandler,
            EndpointSetting coreEndpointConfiguration) {
        this.globalConfig = globalConfig;
        this.request = coreHttpRequest;
        this.responseHandler = responseHandler;
        this.endpointConfiguration = coreEndpointConfiguration;
    }

    /**
     * 
     * @return the {@link GlobalConfiguration} instance
     */
    public GlobalConfiguration getGlobalConfig() {
        return this.globalConfig;
    }

    /**
     *
     * @return the {@link Request} instance
     */
    public Request getRequest() {
        return request;
    }

    /**
     * 
     * @return the {@link ResponseHandler} instance
     */
    public ResponseHandler<?, ?> getResponseHandler() {
        return responseHandler;
    }

    /**
     * @return the {@link EndpointConfiguration} instance
     */
    public EndpointSetting getEndpointConfiguration() {
        return endpointConfiguration;
    }

    /**
     * Execute the ApiCall and returns the expected response
     * 
     * @return instance of ResponseType
     * @throws IOException
     * @throws ExceptionType
     */
    public ResponseType execute() throws IOException, ExceptionType {
        Response httpResponse =
                globalConfig.getHttpClient().execute(request, endpointConfiguration);
        return responseHandler.handle(request, httpResponse, globalConfig, endpointConfiguration);
    }

    /**
     * Execute the Api call asynchronously and returns the expected response in CompletableFuture
     * 
     * @return the instance of {@link CompletableFuture}
     * @throws IOException
     * @throws ExceptionType
     */
    public CompletableFuture<ResponseType> executeAsync() {
        return AsyncExecutor.makeHttpCallAsync(() -> request,
                request -> globalConfig.getHttpClient().executeAsync(request,
                        endpointConfiguration),
                (httpRequest, httpResponse) -> responseHandler.handle(httpRequest, httpResponse,
                        globalConfig, endpointConfiguration));
    }

    /**
     * 
     * Builder class for the {@link ApiCall} class
     *
     * @param <ResponseType>
     * @param <ExceptionType>
     */
    public static class Builder<ResponseType, ExceptionType extends CoreApiException> {
        private GlobalConfiguration globalConfig;
        private HttpRequest.Builder requestBuilder = new HttpRequest.Builder();
        private ResponseHandler.Builder<ResponseType, ExceptionType> responseHandlerBuilder =
                new ResponseHandler.Builder<ResponseType, ExceptionType>();

        private EndpointConfiguration.Builder endpointConfigurationBuilder =
                new EndpointConfiguration.Builder();

        /**
         * @param globalConfig the configuration of Http Request
         * @return {@link ApiCall.Builder}
         */
        public Builder<ResponseType, ExceptionType> globalConfig(GlobalConfiguration globalConfig) {
            this.globalConfig = globalConfig;
            return this;
        }

        /**
         * 
         * @param action
         * @return {@link ApiCall.Builder}
         */
        public Builder<ResponseType, ExceptionType> requestBuilder(
                Consumer<HttpRequest.Builder> action) {
            requestBuilder = new HttpRequest.Builder();
            action.accept(requestBuilder);
            return this;
        }

        /**
         * 
         * @param action
         * @return {@link ApiCall.Builder}
         */
        public Builder<ResponseType, ExceptionType> responseHandler(
                Consumer<ResponseHandler.Builder<ResponseType, ExceptionType>> action) {
            responseHandlerBuilder = new ResponseHandler.Builder<ResponseType, ExceptionType>();
            action.accept(responseHandlerBuilder);
            return this;
        }

        /**
         * 
         * @param action
         * @return {@link ApiCall.Builder}
         */
        public Builder<ResponseType, ExceptionType> endpointConfiguration(
                Consumer<EndpointConfiguration.Builder> action) {
            endpointConfigurationBuilder = new EndpointConfiguration.Builder();
            action.accept(endpointConfigurationBuilder);
            return this;
        }

        /**
         * build the {@link ApiCall}
         * 
         * @return the instance of {@link ApiCall}
         * @throws IOException
         */
        public ApiCall<ResponseType, ExceptionType> build() throws IOException {
            return new ApiCall<ResponseType, ExceptionType>(globalConfig,
                    requestBuilder.build(globalConfig), responseHandlerBuilder.build(),
                    endpointConfigurationBuilder.build());
        }
    }
}
