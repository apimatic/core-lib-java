package io.apimatic.core.logger;

import io.apimatic.coreinterfaces.logger.ApiLogger;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLogging;

public class LoggerFactory {
	/**
	 * Returns an instance of {@code ApiLogger} based on the provided {@code ReadonlyLogging} configuration.
	 * If the logger in the configuration is null, it returns an instance of {@code NullSdkLogger}.
	 * Otherwise, it returns an instance of {@code SdkLogger} initialized with the provided configuration.
	 *
	 * @param config the {@code ReadonlyLogging} configuration to be used for creating the logger
	 * @return an instance of {@code ApiLogger} based on the provided configuration
	 */
	public static ApiLogger getLogger(ReadonlyLogging config) {
		if(config.getLogger() == null) {
			return new NullSdkLogger();
		}
		
		return new SdkLogger(config);
	}
}
