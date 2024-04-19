package io.apimatic.core.logger.configurations;

import io.apimatic.coreinterfaces.logger.configuration.ResponseLoggingOptions;

/**
 * Represents options for logging responses.
 */
public final class SdkResponseLoggingOptions
        extends SdkLoggingOptions<SdkResponseLoggingOptions, SdkResponseLoggingOptions.Builder>
        implements ResponseLoggingOptions {

    /**
     * Constructs a new ResponseLogOptions instance with default values.
     * @param builder Builder instance of {@link SdkRequestLoggingOptions.Builder}
     */
    private SdkResponseLoggingOptions(final Builder builder) {
        super(builder);
    }

    /**
     * Converts {@link SdkResponseLoggingOptions} into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "ResponseLogOptions [logBody=" + shouldLogBody() + " logHeaders="
                + shouldLogHeaders() + " excludeHeaders=" + getHeadersToExclude()
                + " includeHeaders" + getHeadersToInclude() + " unmaskHeaders"
                + getHeadersToUnmask() + "]";
    }

    /**
     * Builds a new {@link SdkResponseLoggingOptions.Builder} object. Creates the instance
     * with the current state.
     * @return a new {@link SdkResponseLoggingOptions.Builder} object.
     */
    public Builder newBuilder() {
        return new Builder().logBody(shouldLogBody()).logHeaders(shouldLogHeaders())
                .excludeHeaders(getHeadersToExclude().toArray(new String[0]))
                .includeHeaders(getHeadersToInclude().toArray(new String[0]))
                .unmaskHeaders(getHeadersToUnmask().toArray(new String[0]));
    }

    /**
     * Builder class for ResponseLogOptions.
     */
    public static class Builder extends SdkLoggingOptions.Builder<SdkResponseLoggingOptions, Builder> {

        /**
         * Returns the {@link SdkResponseLoggingOptions.Builder}
         */
        @Override
        protected Builder self() {
            return this;
        }

        /**
         * Constructs a ResponseLogOptions object with the set values.
         * @return The constructed ResponseLogOptions object.
         */
        @Override
        public SdkResponseLoggingOptions build() {
            return new SdkResponseLoggingOptions(this);
        }
    }
}
