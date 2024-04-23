package io.apimatic.core.logger.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents configuration for logging requests and responses.
 * @param <T> Name of your LoggingConfiguration type
 * @param <B> Name of the Builder for your LoggingConfiguration type
 */
public abstract class SdkHttpLoggingConfiguration<T extends SdkHttpLoggingConfiguration<T, B>,
    B extends SdkHttpLoggingConfiguration.Builder<T, B>> extends SdkBaseHttpLoggingConfiguration {

    /**
     * Constructs a new {@link SdkHttpLoggingConfiguration} instance with default values.
     * @param builder Builder instance of {@link SdkHttpLoggingConfiguration.Builder}
     */
    protected SdkHttpLoggingConfiguration(final Builder<T, B> builder) {
        super();
        this.setLogBody(builder.logBody);
        this.setLogHeaders(builder.logHeaders);
        this.excludeHeaders(builder.excludeHeaders.toArray(new String[0]));
        this.includeHeaders(builder.includeHeaders.toArray(new String[0]));
        this.unmaskHeaders(builder.unmaskHeaders.toArray(new String[0]));
    }

    /**
     * Builder class for {@link SdkHttpLoggingConfiguration}.
     * @param <T> Name of your LoggingConfiguration type
     * @param <B> Name of the Builder for your LoggingConfiguration type
     */
    public abstract static class Builder<T extends SdkHttpLoggingConfiguration<T, B>,
            B extends Builder<T, B>> {
        private boolean logBody = false;
        private boolean logHeaders = false;
        private List<String> excludeHeaders = new ArrayList<>();
        private List<String> includeHeaders = new ArrayList<>();
        private List<String> unmaskHeaders = new ArrayList<>();

        /**
         * Sets whether to log the body.
         * @param logBody True to log the body, otherwise false.
         * @return The builder instance.
         */
        public B body(boolean logBody) {
            this.logBody = logBody;
            return self();
        }

        /**
         * Sets whether to log the headers.
         * @param logHeaders True to log the headers, otherwise false.
         * @return The builder instance.
         */
        public B headers(boolean logHeaders) {
            this.logHeaders = logHeaders;
            return self();
        }

        /**
         * Sets the headers to be excluded from logging.
         * @param excludeHeaders The headers to exclude.
         * @return The builder instance.
         */
        public B excludeHeaders(String... excludeHeaders) {
            this.excludeHeaders = Arrays.stream(excludeHeaders).map(String::toLowerCase)
                    .collect(Collectors.toList());
            return self();
        }

        /**
         * Sets the headers to be included in logging.
         * @param includeHeaders The headers to include.
         * @return The builder instance.
         */
        public B includeHeaders(String... includeHeaders) {
            this.includeHeaders = Arrays.stream(includeHeaders).map(String::toLowerCase)
                    .collect(Collectors.toList());
            return self();
        }

        /**
         * Sets the headers to be unmasked in logging.
         * @param unmaskHeaders The headers to unmask in logging.
         * @return The builder instance.
         */
        public B unmaskHeaders(String... unmaskHeaders) {
            this.unmaskHeaders = Arrays.stream(unmaskHeaders).map(String::toLowerCase)
                    .collect(Collectors.toList());
            return self();
        }

        protected abstract B self();

        /**
         * Constructs a {@link SdkHttpLoggingConfiguration} object with the set values.
         * @return The constructed {@link SdkHttpLoggingConfiguration} object.
         */
        public abstract T build();
    }
}
