package io.apimatic.core.logger;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.event.Level;

import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.ApiLogger;
import io.apimatic.coreinterfaces.logger.Logger;
import io.apimatic.coreinterfaces.logger.configuration.LoggingConfiguration;
import io.apimatic.coreinterfaces.logger.configuration.RequestLoggingConfiguration;
import io.apimatic.coreinterfaces.logger.configuration.ResponseLoggingConfiguration;

/**
 * Class to log the Http api messages.
 */
public class SdkLogger implements ApiLogger {
    /**
     * An instance of {@link Logger}.
     */
    private final Logger logger;

    /**
     * An instance of {@link LoggingConfiguration}.
     */
    private final LoggingConfiguration config;

    /**
     * An instance of {@link RequestLoggingConfiguration}
     */
    private final RequestLoggingConfiguration requestLoggingConfiguration;

    /**
     * An instance of {@link ResponseLoggingConfiguration}
     */
    private final ResponseLoggingConfiguration responseLoggingConfiguration;

    /**
     * Default Constructor.
     * @param config {@link LoggingConfiguration} as logging properties.
     */
    public SdkLogger(final LoggingConfiguration config) {
        this.config = config;
        this.logger = config.getLogger();
        this.requestLoggingConfiguration = config.getRequestConfig();
        this.responseLoggingConfiguration = config.getResponseConfig();
    }

    /**
     * Log requests.
     * @param request HttpRequest to be logged.
     */
    public void logRequest(Request request) {
        Level level = config.getLevel() != null ? config.getLevel() : Level.INFO;

        String contentType = request.getHeaders().value(LoggerConstants.CONTENT_TYPE_HEADER);
        String url = getRequestUrl(request);

        Map<String, Object> requestArguments = new LinkedHashMap<String, Object>();
        requestArguments.put(LoggerConstants.METHOD, request.getHttpMethod());
        requestArguments.put(LoggerConstants.URL, url);
        requestArguments.put(LoggerConstants.CONTENT_TYPE, contentType);
        logger.log(level, "Request {} {} {}", requestArguments);

        if (requestLoggingConfiguration.shouldLogHeaders()) {
            Map<String, String> headersToLog = LoggerUtilities.getHeadersToLog(
                    requestLoggingConfiguration,
                    request.getHeaders().asSimpleMap(),
                    config.getMaskSensitiveHeaders());

            Map<String, Object> requestHeaderArguments = new LinkedHashMap<String, Object>();
            requestHeaderArguments.put(LoggerConstants.HEADERS, headersToLog);
            logger.log(level, "Request Headers {}", requestHeaderArguments);
        }

        if (requestLoggingConfiguration.shouldLogBody()) {
            Object body = request.getBody() != null ? request.getBody() : request.getParameters();
            Map<String, Object> requestBodyArguments = new LinkedHashMap<String, Object>();
            requestBodyArguments.put(LoggerConstants.BODY, body);
            logger.log(level, "Request Body {}", requestBodyArguments);
        }
    }

    /**
     * Log Responses.
     * @param response HttpResponse to be logged.
     */
    public void logResponse(Response response) {
        Level level = config.getLevel() != null ? config.getLevel() : Level.INFO;

        String contentLength = response.getHeaders().value(LoggerConstants.CONTENT_LENGTH_HEADER);
        String contentType = response.getHeaders().value(LoggerConstants.CONTENT_TYPE_HEADER);

        Map<String, Object> responseArguments = new LinkedHashMap<String, Object>();
        responseArguments.put(LoggerConstants.STATUS_CODE, response.getStatusCode());
        responseArguments.put(LoggerConstants.CONTENT_TYPE, contentType);
        responseArguments.put(LoggerConstants.CONTENT_LENGTH, contentLength);
        logger.log(level, "Response {} {} {}", responseArguments);

        if (responseLoggingConfiguration.shouldLogHeaders()) {
            Map<String, String> headersToLog = LoggerUtilities.getHeadersToLog(
                    responseLoggingConfiguration,
                    response.getHeaders().asSimpleMap(),
                    config.getMaskSensitiveHeaders());

            Map<String, Object> responseHeaderArguments = new LinkedHashMap<String, Object>();
            responseHeaderArguments.put(LoggerConstants.HEADERS, headersToLog);
            logger.log(level, "Response Headers {}", responseHeaderArguments);
        }

        if (responseLoggingConfiguration.shouldLogBody()) {
            Map<String, Object> responseBodyArguments = new LinkedHashMap<String, Object>();
            responseBodyArguments.put(LoggerConstants.BODY, response.getBody());
            logger.log(level, "Response Body {}", responseBodyArguments);
        }
    }

    /**
     * Retrieves the URL from the provided request
     * @param request The request object containing the URL and query parameters.
     * @return The URL to be logged
     */
    private String getRequestUrl(Request request) {
        if (requestLoggingConfiguration.shouldIncludeQueryInPath()) {
            return request.getQueryUrl();
        }

        return request.getUrl();
    }
}
