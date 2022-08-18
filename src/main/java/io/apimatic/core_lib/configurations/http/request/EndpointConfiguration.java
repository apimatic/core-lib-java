package io.apimatic.core_lib.configurations.http.request;

import io.apimatic.core_interfaces.http.request.ArraySerializationFormat;
import io.apimatic.core_interfaces.http.request.configuration.CoreEndpointConfiguration;
import io.apimatic.core_interfaces.http.request.configuration.RequestRetryConfiguration;

public class EndpointConfiguration implements CoreEndpointConfiguration {

    private final boolean hasBinaryResponse;
    private final RequestRetryConfiguration requestRetryConfiguration;
    private final ArraySerializationFormat arraySerializationFormat;

    public EndpointConfiguration(boolean hasBinary,
            RequestRetryConfiguration requestRetryConfiguration,
            ArraySerializationFormat arraySerializationFormat) {
        this.hasBinaryResponse = hasBinary;
        this.requestRetryConfiguration = requestRetryConfiguration;
        this.arraySerializationFormat = arraySerializationFormat;
    }

    public RequestRetryConfiguration getRequestRetryConfiguration() {
        return requestRetryConfiguration;
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
        private boolean hasBinaryResponse = false;
        private RequestRetryConfiguration requestRetryConfiguration = new RetryConfiguration.Builder().build();
        private ArraySerializationFormat arraySerializationFormat = ArraySerializationFormat.INDEXED;

        public Builder hasBinaryResponse(boolean hasBinary) {
            this.hasBinaryResponse = hasBinary;
            return this;
        }

        public Builder requestRetryConfiguration(
                RequestRetryConfiguration requestRetryConfiguration) {
            this.requestRetryConfiguration = requestRetryConfiguration;
            return this;
        }

        public Builder arraySerializationFormat(ArraySerializationFormat arraySerializationFormat) {
            this.arraySerializationFormat = arraySerializationFormat;
            return this;
        }

        public EndpointConfiguration build() {
            return new EndpointConfiguration(hasBinaryResponse, requestRetryConfiguration,
                    arraySerializationFormat);
        }

    }


}
