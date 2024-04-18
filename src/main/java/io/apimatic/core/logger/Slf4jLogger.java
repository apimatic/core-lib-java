package io.apimatic.core.logger;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

public class Slf4jLogger implements io.apimatic.coreinterfaces.logger.Logger {

    /**
     * The SLF4J logger instance wrapped by this class.
     */
    private final Logger logger;

    /**
     * Constructs a new Slf4jLogger instance wrapping the provided SLF4J Logger.
     * @param logger The SLF4J logger instance to wrap.
     */
    public Slf4jLogger(Logger logger) {
        this.logger = logger;
    }

    /***
     * Log provided message according to logging level.
     * @param level     To provide the Level conversion.
     * @param format    The format string
     * @param arguments List of arguments
     */
    @Override
    public void log(Level level, String format, Map<String, Object> arguments) {
        LoggingEventBuilder builder = logger.atLevel(level);

        for (Map.Entry<String, Object> entry : arguments.entrySet()) {
            builder.addKeyValue(entry.getKey(), entry.getValue());
        }
        builder.log(format, arguments.values().toArray());
    }
}
