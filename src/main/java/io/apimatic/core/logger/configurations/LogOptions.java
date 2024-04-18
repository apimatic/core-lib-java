package io.apimatic.core.logger.configurations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents options for logging requests and responses.
 */
public abstract class LogOptions<T extends LogOptions<T, B>, B extends LogOptions.Builder<T, B>> extends LogBaseOptions {
    protected boolean logBody = false;
    protected boolean logHeaders = false;
    protected List<String> excludeHeaders = new ArrayList<>();
    protected List<String> includeHeaders = new ArrayList<>();

    /**
     * Constructs a new LogOptions instance with default values.
     * @param builder Builder instance of {@link LogOptions.Builder}
     */
    protected LogOptions(final Builder<T, B> builder) {
        super();
        this.setLogBody(builder.logBody);
        this.setLogHeaders(builder.logHeaders);
        this.excludeHeaders(builder.excludeHeaders.toArray(new String[0]));
        this.includeHeaders(builder.includeHeaders.toArray(new String[0]));
    }

    /**
     * Builder class for LogOptions.
     */
    public static abstract class Builder<T extends LogOptions<T, B>, B extends Builder<T, B>> {
        protected boolean logBody = false;
        protected boolean logHeaders = false;
        protected List<String> excludeHeaders = new ArrayList<>();
        protected List<String> includeHeaders = new ArrayList<>();

        /**
         * Sets whether to log the body.
         * @param logBody True to log the body, otherwise false.
         * @return The builder instance.
         */
        public B logBody(boolean logBody) {
            this.logBody = logBody;
            return self();
        }

        /**
         * Sets whether to log the headers.
         * @param logHeaders True to log the headers, otherwise false.
         * @return The builder instance.
         */
        public B logHeaders(boolean logHeaders) {
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

        protected abstract B self();

        /**
         * Constructs a LogOptions object with the set values.
         * @return The constructed LogOptions object.
         */
        public abstract T build();
    }
}
