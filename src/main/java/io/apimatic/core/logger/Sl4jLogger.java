package io.apimatic.core.logger;

import org.slf4j.Logger;

import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.http.LoggingLevelType;
import io.apimatic.coreinterfaces.logger.Loggable;

public class Sl4jLogger implements Loggable {
	private Logger logger;
	
	public Sl4jLogger(Logger logger) {
		this.logger = logger;
	}
	
	/***
	 * Log provided message according to logging level.
	 * 
	 * @param level     To provide the LoggingLevelType conversion.
	 * @param format    The format string
	 * @param arguments List of arguments
	 */
	@Override
	public void log(LoggingLevel level, String format, Object... arguments) {
		switch (LoggingLevelType.valueOf(level.toString())) {
		case TRACE:
			logger.trace(format, arguments);
			break;
		case DEBUG:
			logger.debug(format, arguments);
			break;
		case INFO:
			logger.info(format, arguments);
			break;
		case WARN:
			logger.warn(format, arguments);
			break;
		case ERROR:
			logger.error(format, arguments);
			break;
		default:
			break;
		}
	}

}
