package io.apimatic.core.logger;

import org.slf4j.Logger;
import org.slf4j.event.Level;

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
	public void log(Level level, String format, Object... arguments) {
		switch (level) {
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
