package io.apimatic.core.configurations.http.client;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import io.apimatic.core.logger.ConsoleLogger;
import io.apimatic.core.logger.Slf4jLogger;
import io.apimatic.core.logger.configurations.RequestLogOptions;
import io.apimatic.core.logger.configurations.ResponseLogOptions;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLoggingConfiguration;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyRequestLogOptions;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyResponseLogOptions;

/**
 * To hold logging configuration.
 */
public final class ApiLoggingConfiguration implements ReadonlyLoggingConfiguration {
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
    private RequestLogOptions requestLogOptions;

    /**
     * Options for logging responses.
     */
    private ResponseLogOptions responseLogOptions;

    /**
     * @param level
     */
    private ApiLoggingConfiguration(final io.apimatic.coreinterfaces.logger.Logger logger, final Level level,
            final boolean maskSensitiveHeaders, final RequestLogOptions requestLogOptions,
            final ResponseLogOptions responseLogOptions) {
        this.logger = logger;
        this.level = level;
        this.maskSensitiveHeaders = maskSensitiveHeaders;
        this.requestLogOptions = requestLogOptions;
        this.responseLogOptions = responseLogOptions;
    }

    /**
     * Getter for Logger.
     *
     * @return Logger instance.
     */
    public io.apimatic.coreinterfaces.logger.Logger getLogger() {
        return logger;
    }

    /**
     * Getter for level.
     *
     * @return Level of logging.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Getter for mask sensitive header.
     *
     * @return True if masking of sensitive headers is enabled, otherwise false.
     */
    public boolean getMaskSensitiveHeaders() {
        return maskSensitiveHeaders;
    }

    /**
     * Getter for the RequestLogOptions.
     *
     * @return The RequestLogOptions object.
     */
    public ReadonlyRequestLogOptions getRequestLogOptions() {
        return requestLogOptions;
    }

    /**
     * Getter for the ResponseLogOptions.
     *
     * @return The ResponseLogOptions object.
     */
    public ReadonlyResponseLogOptions getResponseLogOptions() {
        return responseLogOptions;
    }

    /**
     * Converts this LoggingConfiguration into string format.
     *
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "LoggingConfiguration [logger=" + getLogger() + " level=" + getLevel() + " maskSensitiveHeaders="
                + getMaskSensitiveHeaders() + " requestLogOptions" + getRequestLogOptions() + " responseLogOptions"
                + getResponseLogOptions() + "]";
    }

    /**
     * Builds a new {@link ApiLoggingConfiguration.Builder} object. Creates the
     * instance with the current state.
     *
     * @return a new {@link ApiLoggingConfiguration.Builder} object.
     */
    public Builder newBuilder() {
        return new Builder().logger(logger).level(level).maskSensitiveHeaders(maskSensitiveHeaders)
                .requestLogOptions(builder -> builder = requestLogOptions.newBuilder())
                .responseLogOptions(builder -> builder = responseLogOptions.newBuilder());
    }

    /**
     * Class to build instances of {@link ApiLoggingConfiguration}.
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
        private RequestLogOptions.Builder requestLogOptionsBuilder = new RequestLogOptions.Builder();

        /**
         * Options for logging responses.
         */
        private ResponseLogOptions.Builder responseLogOptionsBuilder = new ResponseLogOptions.Builder();

        /***
         * Set Logger for logging.
         *
         * @param logger The slf4j logger implementation.
         * @return {@link ApiLoggingConfiguration.Builder}.
         */
        public Builder logger(Logger logger) {
            this.logger = new Slf4jLogger(logger);
            return this;
        }

        /***
         * Set Logger wrapper for logging.
         *
         * @param logger The logger wrapper instance
         * @return {@link ApiLoggingConfiguration.Builder}.
         */
        private Builder logger(io.apimatic.coreinterfaces.logger.Logger logger) {
            this.logger = logger;
            return this;
        }

        /**
         * Set level for logging.
         *
         * @param level specify level of all logs.
         * @return {@link ApiLoggingConfiguration.Builder}.
         */
        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        /**
         * Set mask sensitive headers flag.
         *
         * @param maskSensitiveHeaders flag to enable disable masking.
         * @return {@link ApiLoggingConfiguration.Builder}.
         */
        public Builder maskSensitiveHeaders(boolean maskSensitiveHeaders) {
            this.maskSensitiveHeaders = maskSensitiveHeaders;
            return this;
        }

        /**
         * Sets the RequestLogOptions.Builder for the builder.
         *
         * @param action The RequestOptions Builder object.
         * @return {@link ApiLoggingConfiguration.Builder}.
         */
        public Builder requestLogOptions(Consumer<RequestLogOptions.Builder> action) {
            if (requestLogOptionsBuilder != null) {
                action.accept(this.requestLogOptionsBuilder);
            }
            return this;
        }

        /**
         * Sets the ResponseLogOptions.Builder for the builder.
         *
         * @param action The ResponseOptions Builder object.
         * @return {@link ApiLoggingConfiguration.Builder}.
         */
        public Builder responseLogOptions(Consumer<ResponseLogOptions.Builder> action) {
            if (responseLogOptionsBuilder != null) {
                action.accept(this.responseLogOptionsBuilder);
            }
            return this;
        }

        /**
         * Sets the logger instance to ConsoleLogger.
         *
         * @return {@link ApiLoggingConfiguration.Builder}.
         */
        public Builder useConsole() {
            this.logger = new ConsoleLogger();
            return this;
        }

        /**
         * Builds a new LoggingConfiguration object using the set fields.
         *
         * @return {@link ApiLoggingConfiguration}.
         */
        public ApiLoggingConfiguration build() {
            RequestLogOptions requestLogOptions = requestLogOptionsBuilder.build();
            ResponseLogOptions responseLogOptions = responseLogOptionsBuilder.build();
            return new ApiLoggingConfiguration(logger, level, maskSensitiveHeaders, requestLogOptions,
                    responseLogOptions);
        }
    }
}
