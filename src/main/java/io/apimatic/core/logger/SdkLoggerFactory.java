package io.apimatic.core.logger;

import io.apimatic.coreinterfaces.logger.ApiLogger;
import io.apimatic.coreinterfaces.logger.configuration.LoggingConfiguration;

public final class SdkLoggerFactory {

    /**
     * Private constructor to prevent instantiation
     */
    private SdkLoggerFactory() {
    }

    /**
     * Returns an instance of {@link ApiLogger} based on the provided
     * {@code ReadonlyLogging} configuration. If the logger in the configuration is
     * null, it returns an instance of {@code NullSdkLogger}. Otherwise, it returns
     * an instance of {@code SdkLogger} initialized with the provided configuration.
     * @param config the {@link LoggingConfiguration} configuration to be
     *               used for creating the logger
     * @return an instance of {@link ApiLogger} based on the provided configuration
     */
    public static ApiLogger getLogger(LoggingConfiguration config) {
        if (config.getLogger() == null) {
            return new NullSdkLogger();
        }

        return new SdkLogger(config);
    }
}
