package io.apimatic.core.logger;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.helpers.MessageFormatter;

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
     * @param url String request URL.
     */
    public void logRequest(Request request, String url) {
        this.logRequest(request, url, null);
    }
    
    /**
     * Log requests.
     * @param request HttpRequest to be logged.
     * @param url String request URL.
     * @param additionalMessage Any additional message to be logged.
     */
    public void logRequest(Request request, String url, String additionalMessage) {
    	log("Request - Url: {}, HttpMethod: {}, ContentType: {}, ContentLength: {}",
    		 url,
        	 request.getHttpMethod(),
        	 request.getHeaders().value("content-type"),
        	 request.getHeaders().value("content-length"));
    }

    /**
     * Log Responses.
     * @param request HttpRequest that completed.
     * @param response HttpResponse to be logged.
     */
    public void logResponse(Request request, Response response) {
    	log("Response - ContentType: {}, ContentLength: {}",
			 response.getHeaders().value("content-type"),
			 response.getHeaders().value("content-length"));
    }
    
    private void log(String format, Object... arguments) {
    	if(config.getEnableDefaultConsoleLogging()) {
            String message = MessageFormatter.arrayFormat(format, arguments).getMessage();
    		System.out.println(message);
    		return;
    	}
    	logger.info(format, arguments);
    }

    public void addToScope(String key, String val) {
        MDC.put(key, val);
    }

    public void clearScope() {
        MDC.clear();
    }

	/**
     * Log error occurred on executing Request
     * @param request HttpRequest to be logged.
     * @param error Throwable occurred
	 */
	public void logRequestError(Request request, String url, Throwable error) {
		log("Request - Url: {}, HttpMethod: {}, ContentType: {}, ContentLength: {}",
			 url,
	         request.getHttpMethod(),
	         request.getHeaders().value("content-type"),
	         request.getHeaders().value("content-length"));
	}
}
