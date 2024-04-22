package io.apimatic.core.logger.configurations;

import io.apimatic.coreinterfaces.logger.configuration.RequestLoggingConfiguration;

/**
 * Represents options for logging requests.
 */
public final class SdkRequestLoggingConfiguration
        extends SdkHttpLoggingConfiguration<SdkRequestLoggingConfiguration,
            SdkRequestLoggingConfiguration.Builder> implements RequestLoggingConfiguration {

    private boolean includeQueryInPath = true;

    /**
     * Constructs a new RequestLoggingConfiguration instance with default values.
     * @param builder Builder instance of {@link SdkRequestLoggingConfiguration.Builder}
     */
    private SdkRequestLoggingConfiguration(final Builder builder) {
        super(builder);
        this.includeQueryInPath = builder.includeQueryInPath;
    }

    /**
     * Checks if query parameters are included in the request path.
     * @return True if query parameters are included in the path, otherwise false.
     */
    public boolean shouldIncludeQueryInPath() {
        return includeQueryInPath;
    }

    /**
     * Converts {@link SdkRequestLoggingConfiguration} into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "RequestLoggingConfiguration [logBody=" + shouldLogBody() + " logHeaders="
                + shouldLogHeaders() + " includeQueryInPath=" + shouldIncludeQueryInPath()
                + " excludeHeaders=" + getHeadersToExclude() + " includeHeaders"
                + getHeadersToInclude() + " unmaskHeaders" + getHeadersToUnmask() + "]";
    }

    /**
     * Builds a new {@link SdkRequestLoggingConfiguration.Builder} object. Creates the
     * instance with the current state.
     * @return a new {@link SdkRequestLoggingConfiguration.Builder} object.
     */
    public Builder newBuilder() {
        return new Builder().body(shouldLogBody()).headers(shouldLogHeaders())
                .excludeHeaders(getHeadersToExclude().toArray(new String[0]))
                .includeHeaders(getHeadersToInclude().toArray(new String[0]))
                .unmaskHeaders(getHeadersToUnmask().toArray(new String[0]))
                .includeQueryInPath(shouldIncludeQueryInPath());
    }

    /**
     * Builder class for {@link SdkHttpLoggingConfiguration}.
     */
    public static class Builder
            extends SdkHttpLoggingConfiguration.Builder<SdkRequestLoggingConfiguration, Builder> {
        private boolean includeQueryInPath = false;

        /**
         * Sets whether to include query parameters in the request path.
         * @param includeQueryInPath True to include query parameters in the path,
         *                           otherwise false.
         * @return The builder instance.
         */
        public Builder includeQueryInPath(boolean includeQueryInPath) {
            this.includeQueryInPath = includeQueryInPath;
            return this;
        }

        /**
         * Returns the {@link SdkRequestLoggingConfiguration.Builder}
         */
        @Override
        protected Builder self() {
            return this;
        }

        /**
         * Constructs a RequestLoggingConfiguration object with the set values.
         * @return The constructed RequestLoggingConfiguration object.
         */
        @Override
        public SdkRequestLoggingConfiguration build() {
            return new SdkRequestLoggingConfiguration(this);
        }
    }
}
