package io.apimatic.core.logger.configurations;

import io.apimatic.coreinterfaces.logger.configuration.ReadonlyResponseLogOptions;

/**
 * Represents options for logging responses.
 */
public final class ResponseLogOptions extends LogOptions<ResponseLogOptions, ResponseLogOptions.Builder> implements ReadonlyResponseLogOptions {

    /**
     * Constructs a new ResponseLogOptions instance with default values.
     *
     * @param builder Builder instance of {@link RequestLogOptions.Builder}
     */
    private ResponseLogOptions(final Builder builder) {
        super(builder);
    }

    /**
     * Builds a new {@link ResponseLogOptions.Builder} object. Creates the instance
     * with the current state.
     *
     * @return a new {@link ResponseLogOptions.Builder} object.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Builder class for ResponseLogOptions.
     */
    public static class Builder extends LogOptions.Builder<ResponseLogOptions, Builder> {

        @Override
        protected Builder self() {
            return this;
        }

        /**
         * Constructs a ResponseLogOptions object with the set values.
         *
         * @return The constructed ResponseLogOptions object.
         */
        @Override
        public ResponseLogOptions build() {
            return new ResponseLogOptions(this);
        }
    }
}
