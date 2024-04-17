package io.apimatic.core.logger;

import org.slf4j.helpers.MessageFormatter;

import io.apimatic.coreinterfaces.http.LoggingLevel;
import io.apimatic.coreinterfaces.logger.Loggable;

public class ConsoleLogger implements Loggable {

	@Override
	public void log(LoggingLevel level, String format, Object... arguments) {
		StringBuilder builder = new StringBuilder();
		builder.append(level);
		builder.append(": ");
		builder.append(MessageFormatter.basicArrayFormat(format, arguments));
		System.out.println(builder.toString());
	}
}