package io.apimatic.core_lib;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import io.apimatic.core_interfaces.http.request.CoreHttpRequest;
import io.apimatic.core_interfaces.http.request.configuration.CoreEndpointConfiguration;
import io.apimatic.core_interfaces.http.response.CoreHttpResponse;
import io.apimatic.core_lib.configurations.http.request.EndpointConfiguration;
import io.apimatic.core_lib.request.async.AsyncExecutor;
import io.apimatic.core_lib.types.ApiException;

public class ApiCall<ResponseType, ExceptionType extends ApiException> {

    private final CoreConfig coreConfig;
    private final CoreHttpRequest request;
    private final CoreResponseHandler<ResponseType, ExceptionType> responseHandler;
    private final CoreEndpointConfiguration endpointConfiguration;

    private ApiCall(CoreConfig coreConfig, CoreHttpRequest coreHttpRequest,
            CoreResponseHandler<ResponseType, ExceptionType> coreResponseHandler,
            CoreEndpointConfiguration coreEndpointConfiguration) {
        this.coreConfig = coreConfig;
        this.request = coreHttpRequest;
        this.responseHandler = coreResponseHandler;
        this.endpointConfiguration = coreEndpointConfiguration;
    }

    /**
     * Getter for the CoreConfig
     * 
     * @return the CoreConfig instance
     */
    public CoreConfig getCoreConfig() {
        return this.coreConfig;
    }

    /**
     * Getter for the HttpRequest
     * 
     * @return the HttpRequest instance
     */
    public CoreHttpRequest getRequest() {
        return request;
    }

    /**
     * Getter for the ResponseHandler
     * 
     * @return the ResponseHandler
     */
    public CoreResponseHandler<?, ?> getResponseHandler() {
        return responseHandler;
    }

    /**
     * @return the endpointConfiguration
     */
    public CoreEndpointConfiguration getEndpointConfiguration() {
        return endpointConfiguration;
    }

    public ResponseType execute() throws IOException, ExceptionType {
        CoreHttpResponse httpResponse = 
                coreConfig.getHttpClient().execute(request, endpointConfiguration);
        return responseHandler.handle(request, httpResponse, coreConfig);
    }

    public CompletableFuture<ResponseType> executeAsync() throws IOException, ExceptionType {
        return new AsyncExecutor(coreConfig).makeHttpCallAsync(() -> request,
                request -> coreConfig.getHttpClient().executeAsync(request, endpointConfiguration),
                (httpRequest, httpResponse, coreconfig) -> responseHandler.handle(httpRequest,
                        httpResponse, coreConfig));
    }

    public static class Builder<ResponseType, ExceptionType extends ApiException> {
        private CoreConfig coreConfig;
        private CoreRequest.Builder requestBuilder = new CoreRequest.Builder();
        private CoreResponseHandler.Builder<ResponseType, ExceptionType> responseHandlerBuilder =
                new CoreResponseHandler.Builder<ResponseType, ExceptionType>();

        private EndpointConfiguration.Builder endpointConfigurationBuilder =
                new EndpointConfiguration.Builder();

        public Builder<ResponseType, ExceptionType> coreConfig(CoreConfig coreConfig) {
            this.coreConfig = coreConfig;
            return this;
        }

        public Builder<ResponseType, ExceptionType> requestBuilder(
                Consumer<CoreRequest.Builder> action) {
            requestBuilder = new CoreRequest.Builder();
            action.accept(requestBuilder);
            return this;
        }

        public Builder<ResponseType, ExceptionType> responseHandler(
                Consumer<CoreResponseHandler.Builder<ResponseType, ExceptionType>> action) {
            responseHandlerBuilder = new CoreResponseHandler.Builder<ResponseType, ExceptionType>();
            action.accept(responseHandlerBuilder);
            return this;
        }

        public Builder<ResponseType, ExceptionType> endpointConfiguration(
                Consumer<EndpointConfiguration.Builder> action) {
            endpointConfigurationBuilder = new EndpointConfiguration.Builder();
            action.accept(endpointConfigurationBuilder);
            return this;
        }

        public ApiCall<ResponseType, ExceptionType> build() throws IOException {
            return new ApiCall<ResponseType, ExceptionType>(coreConfig,
                    requestBuilder.build(coreConfig), responseHandlerBuilder.build(),
                    endpointConfigurationBuilder.build());
        }
    }
}
