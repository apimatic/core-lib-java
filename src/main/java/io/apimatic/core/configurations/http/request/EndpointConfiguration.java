package io.apimatic.core.configurations.http.request;

import io.apimatic.core.GlobalConfiguration;
import io.apimatic.core.HttpRequest;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;
import io.apimatic.coreinterfaces.http.request.configuration.CoreEndpointConfiguration;
import io.apimatic.coreinterfaces.http.request.configuration.RetryOption;

/**
 * The configuration for an endpoint.
 */
public class EndpointConfiguration implements CoreEndpointConfiguration {

    /**
     * A boolean variable for binary response.
     */
    private final boolean hasBinaryResponse;

    /**
     * Retry options enumeration for HTTP request.
     */
    private final RetryOption retryOption;

    /**
     * Enumeration for all ArraySerialization formats.
     */
    private final ArraySerializationFormat arraySerializationFormat;

    /**
     * GlobalConfiguration applicable along with this EndpointConfiguration.
     */
    private final GlobalConfiguration globalConfig;

    /**
     * Mutable request builder for HTTP request re initialization.
     */
    private final HttpRequest.Builder requestBuilder;

    /**
     * @param hasBinary                A boolean variable for binary response.
     * @param retryOption              Retry options enumeration for HTTP request.
     * @param arraySerializationFormat Enumeration for all ArraySerialization
     *                                 formats.
     */
    public EndpointConfiguration(final boolean hasBinary, final RetryOption retryOption,
            final ArraySerializationFormat arraySerializationFormat, final GlobalConfiguration globalConfig,
            final HttpRequest.Builder requestBuilder) {
        this.hasBinaryResponse = hasBinary;
        this.retryOption = retryOption;
        this.arraySerializationFormat = arraySerializationFormat;
        this.globalConfig = globalConfig;
        this.requestBuilder = requestBuilder;
    }

    /**
     * Retry options enumeration for HTTP request
     * 
     * @return the option for the retries {@link RetryOption}.
     */
    public RetryOption getRetryOption() {
        return retryOption;
    }

    /**
     * Endpoint response has the binary response or not.
     * 
     * @return the response is binary or not.
     */
    public boolean hasBinaryResponse() {
        return hasBinaryResponse;
    }

    /**
     * Enumeration for all ArraySerialization formats
     * 
     * @return the array serialization format.
     */
    public ArraySerializationFormat getArraySerializationFormat() {
        return arraySerializationFormat;
    }

    /**
     * GlobalConfiguration applicable along with this EndpointConfiguration.
     * 
     * @return the global configuration.
     */
    public GlobalConfiguration getGlobalConfiguration() {
        return globalConfig;
    }

    /**
     * Mutable request builder for HTTP request re initialization.
     * 
     * @return the request builder instance.
     */
    public HttpRequest.Builder getRequestBuilder() {
        return requestBuilder;
    }

    public static class Builder {
        /**
         * A boolean variable for binary response.
         */
        private boolean hasBinaryResponse;

        /**
         * Retry options enumeration for HTTP request.
         */
        private RetryOption retryOption = RetryOption.DEFAULT;

        /**
         * Enumeration for all ArraySerialization formats.
         */
        private ArraySerializationFormat arraySerializationFormat = ArraySerializationFormat.INDEXED;

        /**
         * Setter for the binary response.
         * 
         * @param hasBinary end point may have binary response.
         * @return {@link EndpointConfiguration.Builder}.
         */
        public Builder hasBinaryResponse(boolean hasBinary) {
            this.hasBinaryResponse = hasBinary;
            return this;
        }

        /**
         * Setter for the {@link RetryOption}.
         * 
         * @param retryOption Retry options enumeration for HTTP request.
         * @return {@link EndpointConfiguration.Builder}.
         */
        public Builder retryOption(RetryOption retryOption) {
            this.retryOption = retryOption;
            return this;
        }

        /**
         * Setter for the arraySerializationFormat.
         * 
         * @param arraySerializationFormat Enumeration for all ArraySerialization
         *                                 formats.
         * @return {@link EndpointConfiguration.Builder}.
         */
        public Builder arraySerializationFormat(ArraySerializationFormat arraySerializationFormat) {
            this.arraySerializationFormat = arraySerializationFormat;
            return this;
        }

        /**
         * Initialise the {@link EndpointConfiguration}.
         * 
         * @param globalConfig
         * @param requestBuilder
         * @return the {@link EndpointConfiguration} instance.
         */
        public EndpointConfiguration build(GlobalConfiguration globalConfig, HttpRequest.Builder requestBuilder) {
            return new EndpointConfiguration(hasBinaryResponse, retryOption, arraySerializationFormat, globalConfig,
                    requestBuilder);
        }
    }
}
