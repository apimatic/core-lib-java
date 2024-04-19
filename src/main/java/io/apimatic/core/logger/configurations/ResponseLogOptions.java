package io.apimatic.core.logger.configurations;

import io.apimatic.coreinterfaces.logger.configuration.ReadonlyResponseLogOptions;

/**
 * Represents options for logging responses.
 */
public final class ResponseLogOptions extends
LogOptions<ResponseLogOptions, ResponseLogOptions.Builder> implements ReadonlyResponseLogOptions {

    /**
     * Constructs a new ResponseLogOptions instance with default values.
     * @param builder Builder instance of {@link RequestLogOptions.Builder}
     */
    private ResponseLogOptions(final Builder builder) {
        super(builder);
    }

    /**
     * Converts {@link ResponseLogOptions} into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "ResponseLogOptions [logBody=" + shouldLogBody() + " logHeaders=" + shouldLogHeaders()
                + " excludeHeaders=" + getHeadersToExclude() + " includeHeaders"
                + getHeadersToInclude() + " unmaskHeaders" + getHeadersToUnmask() + "]";
    }

    /**
     * Builds a new {@link ResponseLogOptions.Builder} object. Creates the instance
     * with the current state.
     * @return a new {@link ResponseLogOptions.Builder} object.
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
    public static class Builder extends LogOptions.Builder<ResponseLogOptions, Builder> {

        /**
         * Returns the {@link ResponseLogOptions.Builder}
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
        public ResponseLogOptions build() {
            return new ResponseLogOptions(this);
        }
    }
}
