package io.apimatic.core.logger;

import java.util.Map;

import org.slf4j.event.Level;
import org.slf4j.helpers.MessageFormatter;

import io.apimatic.coreinterfaces.logger.Logger;

public class ConsoleLogger implements Logger {

    /**
     * Logs a message with the specified logging level, format, and arguments.
     * @param level     the logging level of the message
     * @param format    a format string for the message
     * @param arguments arguments referenced by the format string
     */
    @Override
    public void log(Level level, String format, Map<String, Object> arguments) {
        System.out.println(String.format("%s: %s", level,
                MessageFormatter.basicArrayFormat(format, arguments.values().toArray())));
    }
}
