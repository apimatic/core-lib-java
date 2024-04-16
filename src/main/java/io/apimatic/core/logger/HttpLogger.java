package io.apimatic.core.logger;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import io.apimatic.coreinterfaces.http.LoggingLevelType;
import io.apimatic.coreinterfaces.http.request.ArraySerializationFormat;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.ApiLogger;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLogging;

/**
 * Class to log the Http api messages.
 */
public class HttpLogger implements ApiLogger {
    /**
     * An instance of {@link Logger}.
     */
    private Logger logger;

    /**
     * An instance of {@link ReadonlyLogging}.
     */
    private ReadonlyLogging config;

    /**
     * Default Constructor.
     * @param logger Logger instance for logging.
     * @param config {@link ReadonlyLogging} as logging properties.
     */
    public HttpLogger(final Logger logger, final ReadonlyLogging config) {
        this.logger = logger;
        this.config = config;
    }

    /**
     * Log requests.
     * @param request HttpRequest to be logged.
     * @param arraySerializationFormat Enumeration for all ArraySerialization formats.
     */
    public void logRequest(Request request, ArraySerializationFormat arraySerializationFormat) {
    	log(LoggingLevelType.INFO, "Request {} Url: {}, ContentType: {}.",
			request.getHttpMethod(),
			request.getQueryUrl(),
        	request.getHeaders().value("content-type"));
    }

    /**
     * Log Responses.
     * @param response HttpResponse to be logged.
     */
    public void logResponse(Response response) {
    	log(LoggingLevelType.INFO, "Response StatusCode: {}, ContentType: {}, ContentLength: {}",
    		 response.getStatusCode(),
			 response.getHeaders().value("content-type"),
			 response.getHeaders().value("content-length"));
    }
    
    private void log(LoggingLevelType level, String format, Object... arguments) {
    	if(config.getEnableDefaultConsoleLogging()) {
            String message = MessageFormatter.arrayFormat(format, arguments).getMessage();
    		System.out.println(message);
    		return;
    	}

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
