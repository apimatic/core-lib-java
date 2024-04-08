package io.apimatic.core.configurations.http.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.http.LoggingPolicy;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLogging;

/**
 * To hold logging configuration.
 */
public final class ApiLoggingConfiguration implements ReadonlyLogging {

    /**
     * Level enum to use with level in {@link ApiLoggingConfiguration.Builder}.
     */
    public enum Level {
        /**
         * To log with logging level 'INFO'.
         */
        INFO,

        /**
         * To log with logging level 'Error'.
         */
        ERROR,

        /**
         * To log with logging level 'WARN'.
         */
        WARN,

        /**
         * To log with logging level 'DEBUG'.
         */
        DEBUG,

        /**
         * To log with logging level 'TRACE'.
         */
        TRACE
    }

    /**
     * An instance of {@link LoggingLevel}.
     */
    private LoggingLevel level;
    
    private boolean enableDefaultConsoleLogging;

    /**
     * @param level
     */
    private ApiLoggingConfiguration(final LoggingLevel level, boolean enableDefaultConsoleLogging) {
        this.level = level;
        this.enableDefaultConsoleLogging = enableDefaultConsoleLogging;
    }

    /**
     * Getter for level.
     * @return Level of logging.
     */
    public LoggingLevel getLevel() {
        return level;
    }

    /**
     * Getter for getEnableDefaultConsoleLogging.
     * @return .
     */
    public boolean getEnableDefaultConsoleLogging() {
        return enableDefaultConsoleLogging;
    }
    
    /**
     * Converts this LoggingConfiguration into string format.
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return "LoggingConfiguration [level=" + level + "]";
    }

    /**
     * Builds a new {@link ApiLoggingConfiguration.Builder} object. Creates the instance with the
     * current state.
     * @return a new {@link ApiLoggingConfiguration.Builder} object.
     */
    public Builder newBuilder() {
        return new Builder().level(level)
        		            .enableDefaultConsoleLogging(enableDefaultConsoleLogging);
    }

    /**
     * Class to build instances of {@link ApiLoggingConfiguration}.
     */
    public static class Builder {
        /**
         * An instance of {@link LoggingLevel}.
         */
        private LoggingLevel level = LoggingLevel.INFO;
        
		private boolean enableDefaultConsoleLogging;


        /**
         * Set level for logging.
         * @param level specify level of all logs.
         * @return {@link ApiLoggingConfiguration.Builder}.
         */
        public Builder level(LoggingLevel level) {
            this.level = level;
            return this;
        }

        public Builder enableDefaultConsoleLogging(boolean enableDefaultConsoleLogging) {
			this.enableDefaultConsoleLogging = enableDefaultConsoleLogging;
			return this;
		}

		/**
         * Builds a new LoggingConfiguration object using the set fields.
         * @return {@link ApiLoggingConfiguration}.
         */
        public ApiLoggingConfiguration build() {
            return new ApiLoggingConfiguration(level, enableDefaultConsoleLogging);
        }
    }
}
