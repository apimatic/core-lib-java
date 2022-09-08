package io.apimatic.core_lib.configurations.http.request;

import io.apimatic.core_interfaces.http.request.ArraySerializationFormat;
import io.apimatic.core_interfaces.http.request.configuration.CoreEndpointConfiguration;
import io.apimatic.core_interfaces.http.request.configuration.RetryOption;

public class EndpointConfiguration implements CoreEndpointConfiguration {

    private final boolean hasBinaryResponse;
    private final RetryOption retryOption;
    private final ArraySerializationFormat arraySerializationFormat;

    public EndpointConfiguration(boolean hasBinary,
            RetryOption retryOption,
            ArraySerializationFormat arraySerializationFormat) {
        this.hasBinaryResponse = hasBinary;
        this.retryOption = retryOption;
        this.arraySerializationFormat = arraySerializationFormat;
    }

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
        private ArraySerializationFormat arraySerializationFormat = ArraySerializationFormat.INDEXED;

        public Builder hasBinaryResponse(boolean hasBinary) {
            this.hasBinaryResponse = hasBinary;
            return this;
        }

        public Builder retryOption(
                RetryOption retryOption) {
            this.retryOption = retryOption;
            return this;
        }

        public Builder arraySerializationFormat(ArraySerializationFormat arraySerializationFormat) {
            this.arraySerializationFormat = arraySerializationFormat;
            return this;
        }

        public EndpointConfiguration build() {
            return new EndpointConfiguration(hasBinaryResponse, retryOption,
                    arraySerializationFormat);
        }

    }


}
