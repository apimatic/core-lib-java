package io.apimatic.core.logger.configurations;

import io.apimatic.coreinterfaces.logger.configuration.ResponseLoggingConfiguration;

/**
 * Represents configuration for logging responses.
 */
public final class SdkResponseLoggingConfiguration
        extends SdkHttpLoggingConfiguration<SdkResponseLoggingConfiguration,
                                            SdkResponseLoggingConfiguration.Builder>
        implements ResponseLoggingConfiguration {

    /**
     * Constructs a new ResponseLoggingConfiguration instance with default values.
     * @param builder Builder instance of {@link SdkRequestLoggingConfiguration.Builder}
     */
    private SdkResponseLoggingConfiguration(final Builder builder) {
        super(builder);
    }

    /**
     * Converts {@link SdkResponseLoggingConfiguration} into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "ResponseLoggingConfiguration [logBody=" + shouldLogBody() + " logHeaders="
                + shouldLogHeaders() + " excludeHeaders=" + getHeadersToExclude()
                + " includeHeaders=" + getHeadersToInclude() + " unmaskHeaders="
                + getHeadersToUnmask() + "]";
    }

    /**
     * Builds a new {@link SdkResponseLoggingConfiguration.Builder} object. Creates the
     * instance with the current state.
     * @return a new {@link SdkResponseLoggingConfiguration.Builder} object.
     */
    public Builder newBuilder() {
        return new Builder().body(shouldLogBody()).headers(shouldLogHeaders())
                .excludeHeaders(getHeadersToExclude().toArray(new String[0]))
                .includeHeaders(getHeadersToInclude().toArray(new String[0]))
                .unmaskHeaders(getHeadersToUnmask().toArray(new String[0]));
    }

    /**
     * Builder class for ResponseLoggingConfiguration.
     */
    public static class Builder
            extends SdkHttpLoggingConfiguration.Builder<SdkResponseLoggingConfiguration, Builder> {

        /**
         * Returns the {@link SdkResponseLoggingConfiguration.Builder}
         */
        @Override
        protected Builder self() {
            return this;
        }

        /**
         * Constructs a ResponseLoggingConfiguration object with the set values.
         * @return The constructed ResponseLoggingConfiguration object.
         */
        @Override
        public SdkResponseLoggingConfiguration build() {
            return new SdkResponseLoggingConfiguration(this);
        }
    }
}
