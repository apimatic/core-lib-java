package io.apimatic.core.logger.configurations;

import io.apimatic.coreinterfaces.logger.configuration.RequestLoggingOptions;

/**
 * Represents options for logging requests.
 */
public final class SdkRequestLoggingOptions
        extends SdkLoggingOptions<SdkRequestLoggingOptions, SdkRequestLoggingOptions.Builder>
        implements RequestLoggingOptions {

    private boolean includeQueryInPath = true;

    /**
     * Constructs a new RequestLogOptions instance with default values.
     * @param builder Builder instance of {@link SdkRequestLoggingOptions.Builder}
     */
    private SdkRequestLoggingOptions(final Builder builder) {
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
     * Converts {@link SdkRequestLoggingOptions} into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "RequestLogOptions [logBody=" + shouldLogBody() + " logHeaders=" + shouldLogHeaders()
                + " includeQueryInPath=" + shouldIncludeQueryInPath() + " excludeHeaders="
                + getHeadersToExclude() + " includeHeaders" + getHeadersToInclude()
                + " unmaskHeaders" + getHeadersToUnmask() + "]";
    }

    /**
     * Builds a new {@link SdkRequestLoggingOptions.Builder} object. Creates the
     * instance with the current state.
     * @return a new {@link SdkRequestLoggingOptions.Builder} object.
     */
    public Builder newBuilder() {
        return new Builder().logBody(shouldLogBody()).logHeaders(shouldLogHeaders())
                .excludeHeaders(getHeadersToExclude().toArray(new String[0]))
                .includeHeaders(getHeadersToInclude().toArray(new String[0]))
                .unmaskHeaders(getHeadersToUnmask().toArray(new String[0]))
                .includeQueryInPath(shouldIncludeQueryInPath());
    }

    /**
     * Builder class for RequestLogOptions.
     */
    public static class Builder
            extends SdkLoggingOptions.Builder<SdkRequestLoggingOptions, Builder> {
        private boolean includeQueryInPath = true;

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
         * Returns the {@link SdkRequestLoggingOptions.Builder}
         */
        @Override
        protected Builder self() {
            return this;
        }

        /**
         * Constructs a RequestLogOptions object with the set values.
         * @return The constructed RequestOptions object.
         */
        @Override
        public SdkRequestLoggingOptions build() {
            return new SdkRequestLoggingOptions(this);
        }
    }
}
