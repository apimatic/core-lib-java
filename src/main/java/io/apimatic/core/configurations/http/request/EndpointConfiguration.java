package io.apimatic.core.configurations.http.request;

import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;
import io.apimatic.coreinterfaces.http.request.configuration.EndpointSetting;
import io.apimatic.coreinterfaces.http.request.configuration.RetryOption;

/**
 * The configuration for an endpoint.
 *
 */
public class EndpointConfiguration implements EndpointSetting {

    private final boolean hasBinaryResponse;
    private final RetryOption retryOption;
    private final ArraySerializationFormat arraySerializationFormat;

    public EndpointConfiguration(boolean hasBinary, RetryOption retryOption,
            ArraySerializationFormat arraySerializationFormat) {
        this.hasBinaryResponse = hasBinary;
        this.retryOption = retryOption;
        this.arraySerializationFormat = arraySerializationFormat;
    }

    /**
     * @return the option for the retries
     */
    public RetryOption getRetryOption() {
        return retryOption;
    }

    @Override
    public boolean hasBinaryResponse() {
        return hasBinaryResponse;
    }

    @Override
    public ArraySerializationFormat getArraySerializationFormat() {
        return arraySerializationFormat;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean hasBinaryResponse;
        private RetryOption retryOption = RetryOption.DEFAULT;;
        private ArraySerializationFormat arraySerializationFormat =
                ArraySerializationFormat.INDEXED;

        public Builder hasBinaryResponse(boolean hasBinary) {
            this.hasBinaryResponse = hasBinary;
            return this;
        }

        /**
         * 
         * @param retryOption
         * @return {@link EndpointConfiguration.Builder}
         */
        public Builder retryOption(RetryOption retryOption) {
            this.retryOption = retryOption;
            return this;
        }

        /**
         * 
         * @param arraySerializationFormat
         * @return
         */
        public Builder arraySerializationFormat(ArraySerializationFormat arraySerializationFormat) {
            this.arraySerializationFormat = arraySerializationFormat;
            return this;
        }

        /**
         * Initialise the {@link EndpointConfiguration}
         * 
         * @return the {@link EndpointConfiguration} instance
         */
        public EndpointConfiguration build() {
            return new EndpointConfiguration(hasBinaryResponse, retryOption,
                    arraySerializationFormat);
        }
    }
}
