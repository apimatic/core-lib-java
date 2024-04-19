package io.apimatic.core.logger.configurations;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import io.apimatic.core.logger.ConsoleLogger;
import io.apimatic.core.logger.Slf4jLogger;
import io.apimatic.coreinterfaces.logger.configuration.LoggingConfiguration;
import io.apimatic.coreinterfaces.logger.configuration.RequestLoggingOptions;
import io.apimatic.coreinterfaces.logger.configuration.ResponseLoggingOptions;

/**
 * To hold logging configuration.
 */
public final class SdkLoggingConfiguration implements LoggingConfiguration {
    /***
     * An instance of Logger
     */
    private io.apimatic.coreinterfaces.logger.Logger logger;

    /**
     * An instance of {@link Level}.
     */
    private Level level;

    /**
     * Options for masking sensitive headers
     */
    private boolean maskSensitiveHeaders;

    /**
     * Options for logging requests.
     */
    private RequestLoggingOptions requestLogOptions;

    /**
     * Options for logging responses.
     */
    private ResponseLoggingOptions responseLogOptions;

    /**
     * Constructs an instance of ApiLoggingConfiguration.
     * @param logger               The logger implementation to use for logging API
     *                             requests and responses.
     * @param level                The logging level at which API requests and
     *                             responses will be logged.
     * @param maskSensitiveHeaders A boolean indicating whether sensitive headers
     *                             should be masked in the logs.
     * @param requestLogOptions    The options for logging API request details.
     * @param responseLogOptions   The options for logging API response details.
     */
    private SdkLoggingConfiguration(final io.apimatic.coreinterfaces.logger.Logger logger,
            final Level level, final boolean maskSensitiveHeaders,
            final RequestLoggingOptions requestLogOptions,
            final ResponseLoggingOptions responseLogOptions) {
        this.logger = logger;
        this.level = level;
        this.maskSensitiveHeaders = maskSensitiveHeaders;
        this.requestLogOptions = requestLogOptions;
        this.responseLogOptions = responseLogOptions;
    }

    /**
     * Getter for Logger.
     * @return Logger instance.
     */
    public io.apimatic.coreinterfaces.logger.Logger getLogger() {
        return logger;
    }

    /**
     * Getter for level.
     * @return Level of logging.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Getter for mask sensitive header.
     * @return True if masking of sensitive headers is enabled, otherwise false.
     */
    public boolean getMaskSensitiveHeaders() {
        return maskSensitiveHeaders;
    }

    /**
     * Getter for the RequestLogOptions.
     * @return The RequestLogOptions object.
     */
    public RequestLoggingOptions getRequestLogOptions() {
        return requestLogOptions;
    }

    /**
     * Getter for the ResponseLogOptions.
     * @return The ResponseLogOptions object.
     */
    public ResponseLoggingOptions getResponseLogOptions() {
        return responseLogOptions;
    }

    /**
     * Converts this LoggingConfiguration into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "LoggingConfiguration [logger=" + getLogger() + " level=" + getLevel()
                + " maskSensitiveHeaders=" + getMaskSensitiveHeaders() + " requestLogOptions"
                + getRequestLogOptions() + " responseLogOptions" + getResponseLogOptions() + "]";
    }

    /**
     * Builds a new {@link SdkLoggingConfiguration.Builder} object. Creates the
     * instance with the current state.
     * @return a new {@link SdkLoggingConfiguration.Builder} object.
     */
    public Builder newBuilder() {
        Builder builder = new Builder().logger(logger).level(level)
                .maskSensitiveHeaders(maskSensitiveHeaders);
        builder.requestLogOptionsBuilder = ((SdkRequestLoggingOptions) requestLogOptions).newBuilder();
        builder.responseLogOptionsBuilder = ((SdkResponseLoggingOptions) responseLogOptions).newBuilder();
        return builder;
    }

    /**
     * Class to build instances of {@link SdkLoggingConfiguration}.
     */
    public static class Builder {
        /***
         * An instance of Logger
         */
        private io.apimatic.coreinterfaces.logger.Logger logger = null;
        /**
         * An instance of {@link Level}.
         */
        private Level level;

        /**
         * Options for masking sensitive headers
         */
        private boolean maskSensitiveHeaders = true;

        /**
         * Options for logging requests.
         */
        private SdkRequestLoggingOptions.Builder requestLogOptionsBuilder =
                new SdkRequestLoggingOptions.Builder();

        /**
         * Options for logging responses.
         */
        private SdkResponseLoggingOptions.Builder responseLogOptionsBuilder =
                new SdkResponseLoggingOptions.Builder();

        /***
         * Set Logger for logging.
         * @param logger The slf4j logger implementation.
         * @return {@link SdkLoggingConfiguration.Builder}.
         */
        public Builder logger(Logger logger) {
            this.logger = new Slf4jLogger(logger);
            return this;
        }

        /***
         * Set Logger wrapper for logging.
         * @param logger The logger wrapper instance
         * @return {@link SdkLoggingConfiguration.Builder}.
         */
        private Builder logger(io.apimatic.coreinterfaces.logger.Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * Set level for logging.
         * @param level specify level of all logs.
         * @return {@link SdkLoggingConfiguration.Builder}.
         */
        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        /**
         * Set mask sensitive headers flag.
         * @param maskSensitiveHeaders flag to enable disable masking.
         * @return {@link SdkLoggingConfiguration.Builder}.
         */
        public Builder maskSensitiveHeaders(boolean maskSensitiveHeaders) {
            this.maskSensitiveHeaders = maskSensitiveHeaders;
            return this;
        }

        /**
         * Sets the RequestLogOptions.Builder for the builder.
         * @param action The RequestOptions Builder object.
         * @return {@link SdkLoggingConfiguration.Builder}.
         */
        public Builder requestLogOptions(Consumer<SdkRequestLoggingOptions.Builder> action) {
            if (action != null) {
                action.accept(this.requestLogOptionsBuilder);
            }
            return this;
        }

        /**
         * Sets the ResponseLogOptions.Builder for the builder.
         * @param action The ResponseOptions Builder object.
         * @return {@link SdkLoggingConfiguration.Builder}.
         */
        public Builder responseLogOptions(Consumer<SdkResponseLoggingOptions.Builder> action) {
            if (action != null) {
                action.accept(this.responseLogOptionsBuilder);
            }
            return this;
        }

        /**
         * Sets the logger instance to ConsoleLogger.
         * @return {@link SdkLoggingConfiguration.Builder}.
         */
        public Builder useDefaultLogger() {
            this.logger = new ConsoleLogger();
            return this;
        }

        /**
         * Builds a new LoggingConfiguration object using the set fields.
         * @return {@link SdkLoggingConfiguration}.
         */
        public SdkLoggingConfiguration build() {
            SdkRequestLoggingOptions requestLogOptions = requestLogOptionsBuilder.build();
            SdkResponseLoggingOptions responseLogOptions = responseLogOptionsBuilder.build();
            return new SdkLoggingConfiguration(logger, level, maskSensitiveHeaders,
                    requestLogOptions, responseLogOptions);
        }
    }
}
