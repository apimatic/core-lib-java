package io.apimatic.core.logger;

import org.slf4j.helpers.MessageFormatter;

import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.logger.Logger;

public class ConsoleLogger implements Logger {

	@Override
	public void log(LoggingLevel level, String format, Object... arguments) {
		StringBuilder builder = new StringBuilder();
		builder.append(level);
		builder.append(": ");
		builder.append(MessageFormatter.basicArrayFormat(format, arguments));
		System.out.println(builder.toString());
	}
}