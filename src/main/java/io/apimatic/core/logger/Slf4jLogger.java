package io.apimatic.core.logger;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

public class Slf4jLogger implements io.apimatic.coreinterfaces.logger.Logger {
	private Logger logger;

	public Slf4jLogger(Logger logger) {
		this.logger = logger;
	}

	/***
	 * Log provided message according to logging level.
	 * 
	 * @param level     To provide the Level conversion.
	 * @param format    The format string
	 * @param arguments List of arguments
	 */
	@Override
	public void log(Level level, String format, Map<String, Object> argumentsKvp) {
		LoggingEventBuilder builder = logger.atLevel(level);
		
		for (Map.Entry<String, Object> entry : argumentsKvp.entrySet()) {
			builder.addKeyValue(entry.getKey(), entry.getValue());
		}
		builder.log(format, argumentsKvp.values().toArray());
	}
}
