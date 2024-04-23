package io.apimatic.core.logger.configurations;

import io.apimatic.coreinterfaces.logger.configuration.RequestLoggingConfiguration;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import io.apimatic.core.logger.ConsoleLogger;
import io.apimatic.core.logger.Slf4jLogger;
import io.apimatic.coreinterfaces.logger.configuration.LoggingConfiguration;
import io.apimatic.coreinterfaces.logger.configuration.ResponseLoggingConfiguration;

/**
 * To hold logging configuration.
 */
public final class SdkLoggingConfiguration implements LoggingConfiguration {
    /***
     * An instance of Logger
     */
    private final io.apimatic.coreinterfaces.logger.Logger logger;

    /**
     * An instance of {@link Level}.
     */
    private final Level level;

    /**
     * Configuration for masking sensitive headers
     */
    private final boolean maskSensitiveHeaders;

    /**
     * Configuration for logging requests.
     */
    private final RequestLoggingConfiguration requestLoggingConfiguration;

    /**
     * Configuration for logging responses.
     */
    private final ResponseLoggingConfiguration responseLoggingConfiguration;

    /**
     * Constructs an instance of ApiLoggingConfiguration.
     * @param logger                       The logger implementation to use for logging API
     *                                     requests and responses.
     * @param level                        The logging level at which API requests and
     *                                     responses will be logged.
     * @param maskSensitiveHeaders         A boolean indicating whether sensitive headers
     *                                     should be masked in the logs.
     * @param requestLoggingConfiguration  The configuration for logging API request details.
     * @param responseLoggingConfiguration The configuration for logging API response details.
     */
    private SdkLoggingConfiguration(final io.apimatic.coreinterfaces.logger.Logger logger,
                                    final Level level, final boolean maskSensitiveHeaders,
                                    final RequestLoggingConfiguration requestLoggingConfiguration,
                                    final ResponseLoggingConfiguration
                                            responseLoggingConfiguration) {
        this.logger = logger;
        this.level = level;
        this.maskSensitiveHeaders = maskSensitiveHeaders;
        this.requestLoggingConfiguration = requestLoggingConfiguration;
        this.responseLoggingConfiguration = responseLoggingConfiguration;
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
     * Getter for the RequestLoggingConfiguration.
     * @return The RequestLoggingConfiguration object.
     */
    public RequestLoggingConfiguration getRequestConfig() {
        return requestLoggingConfiguration;
    }

    /**
     * Getter for the ResponseLoggingConfiguration.
     * @return The ResponseLoggingConfiguration object.
     */
    public ResponseLoggingConfiguration getResponseConfig() {
        return responseLoggingConfiguration;
    }

    /**
     * Converts this LoggingConfiguration into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "LoggingConfiguration [logger=" + getLogger() + " level=" + getLevel()
                + " maskSensitiveHeaders=" + getMaskSensitiveHeaders()
                + " requestLoggingConfiguration=" + getRequestConfig()
                + " responseLoggingConfiguration=" + getResponseConfig() + "]";
    }

    /**
     * Builds a new {@link SdkLoggingConfiguration.Builder} object. Creates the
     * instance with the current state.
     * @return a new {@link SdkLoggingConfiguration.Builder} object.
     */
    public Builder newBuilder() {
        Builder builder = new Builder().logger(logger).level(level)
                .maskSensitiveHeaders(maskSensitiveHeaders);
        builder.requestLoggingConfigurationBuilder =
                ((SdkRequestLoggingConfiguration) requestLoggingConfiguration).newBuilder();
        builder.responseLoggingConfigurationBuilder =
                ((SdkResponseLoggingConfiguration) responseLoggingConfiguration).newBuilder();
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
         * Configuration for masking sensitive headers
         */
        private boolean maskSensitiveHeaders = true;

        /**
         * Configuration for logging requests.
         */
        private SdkRequestLoggingConfiguration.Builder requestLoggingConfigurationBuilder =
                new SdkRequestLoggingConfiguration.Builder();

        /**
         * Configuration for logging responses.
         */
        private SdkResponseLoggingConfiguration.Builder responseLoggingConfigurationBuilder =
                new SdkResponseLoggingConfiguration.Builder();

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
        public Builder logger(io.apimatic.coreinterfaces.logger.Logger logger) {
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
         * Sets the RequestLoggingConfiguration.Builder for the builder.
         * @param builder The RequestLoggingConfiguration Builder object.
         * @return {@link SdkLoggingConfiguration.Builder}.
         */
        public Builder requestConfig(SdkRequestLoggingConfiguration.Builder builder) {
            this.requestLoggingConfigurationBuilder = builder;
            return this;
        }

        /**
         * Sets the ResponseLoggingConfiguration.Builder for the builder.
         * @param builder The ResponseLoggingConfiguration Builder object.
         * @return {@link SdkLoggingConfiguration.Builder}.
         */
        public Builder responseConfig(SdkResponseLoggingConfiguration.Builder builder) {
            this.responseLoggingConfigurationBuilder = builder;
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
            SdkRequestLoggingConfiguration requestLoggingConfiguration =
                    requestLoggingConfigurationBuilder.build();
            SdkResponseLoggingConfiguration responseLoggingConfiguration =
                    responseLoggingConfigurationBuilder.build();
            return new SdkLoggingConfiguration(logger, level, maskSensitiveHeaders,
                    requestLoggingConfiguration, responseLoggingConfiguration);
        }
    }
}
