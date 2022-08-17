package io.apimatic.core_lib.configurations.http.request;

import io.apimatic.core_interfaces.http.request.ArraySerializationFormat;
import io.apimatic.core_interfaces.http.request.configuration.CoreEndpointConfiguration;
import io.apimatic.core_interfaces.http.request.configuration.RequestRetryConfiguration;

public class EndpointConfiguration implements CoreEndpointConfiguration {

    private final boolean hasBinary;
    private final RequestRetryConfiguration requestRetryConfiguration;
    private final ArraySerializationFormat arraySerializationFormat;

    public EndpointConfiguration(boolean hasBinary,
            RequestRetryConfiguration requestRetryConfiguration,
            ArraySerializationFormat arraySerializationFormat) {
        this.hasBinary = hasBinary;
        this.requestRetryConfiguration = requestRetryConfiguration;
        this.arraySerializationFormat = arraySerializationFormat;
    }

    public RequestRetryConfiguration getRequestRetryConfiguration() {
        return requestRetryConfiguration;
    }

    @Override
    public boolean hasBinary() {
        return hasBinary;
    }

    @Override
    public ArraySerializationFormat getArraySerializationFormat() {
        return arraySerializationFormat;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean hasBinary;
        private RequestRetryConfiguration requestRetryConfiguration;
        private ArraySerializationFormat arraySerializationFormat;

        public Builder hasBinary(boolean hasBinary) {
            this.hasBinary = hasBinary;
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
            return new EndpointConfiguration(hasBinary, requestRetryConfiguration,
                    arraySerializationFormat);
        }

    }


}
