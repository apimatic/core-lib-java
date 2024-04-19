package io.apimatic.core.logger;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.event.Level;

import io.apimatic.core.utilities.CoreHelper;
import io.apimatic.coreinterfaces.http.request.Request;
import io.apimatic.coreinterfaces.http.response.Response;
import io.apimatic.coreinterfaces.logger.ApiLogger;
import io.apimatic.coreinterfaces.logger.Logger;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyLoggingConfiguration;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyRequestLogOptions;
import io.apimatic.coreinterfaces.logger.configuration.ReadonlyResponseLogOptions;

/**
 * Class to log the Http api messages.
 */
public class SdkLogger implements ApiLogger {
    /**
     * An instance of {@link Logger}.
     */
    private Logger logger;

    /**
     * An instance of {@link ReadonlyLoggingConfiguration}.
     */
    private ReadonlyLoggingConfiguration config;

    /**
     * An instance of {@link ReadonlyRequestLogOptions}
     */
    private ReadonlyRequestLogOptions requestLogOptions;

    /**
     * An instance of {@link ReadonlyResponseLogOptions}
     */
    private ReadonlyResponseLogOptions responseLogOptions;

    /**
     * Default Constructor.
     *
     * @param config {@link ReadonlyLoggingConfiguration} as logging properties.
     */
    public SdkLogger(final ReadonlyLoggingConfiguration config) {
        this.config = config;
        this.logger = config.getLogger();
        this.requestLogOptions = config.getRequestLogOptions();
        this.responseLogOptions = config.getResponseLogOptions();
    }

    /**
     * Log requests.
     *
     * @param request HttpRequest to be logged.
     */
    public void logRequest(Request request) {
        Level level = config.getLevel() != null ? config.getLevel() : Level.INFO;

        String url = request.getUrl();
        String queryParameter = CoreHelper.getQueryParametersFromUrl(request.getQueryUrl());
        String contentType = request.getHeaders().value(LoggerConstants.CONTENT_TYPE) != null
                ? request.getHeaders().value(LoggerConstants.CONTENT_TYPE)
                : "";

        Map<String, Object> requestArguments = new LinkedHashMap<String, Object>();
        requestArguments.put(LoggerConstants.METHOD, request.getHttpMethod());
        requestArguments.put(LoggerConstants.URL, url);
        requestArguments.put(LoggerConstants.CONTENT_TYPE, contentType);
        if (requestLogOptions.shouldIncludeQueryInPath()) {
            requestArguments.put(LoggerConstants.QUERY_PARAMETER, queryParameter);
            logger.log(level, "Request {} {} {} queryParameters: {}", requestArguments);
        } else {
            logger.log(level, "Request {} {} {}", requestArguments);
        }

        if (requestLogOptions.shouldLogHeaders()) {
            Map<String, String> headersToLog = LoggerUtilities.getHeadersToLog(requestLogOptions,
                    request.getHeaders().asSimpleMap(), config.getMaskSensitiveHeaders());

            Map<String, Object> requestHeaderArguments = new LinkedHashMap<String, Object>();
            requestHeaderArguments.put(LoggerConstants.HEADERS, headersToLog);
            logger.log(level, "Request Headers {}", requestHeaderArguments);
        }

        if (requestLogOptions.shouldLogBody()) {
            Object body = request.getBody() != null ? request.getBody() : request.getParameters();
            Map<String, Object> requestBodyArguments = new LinkedHashMap<String, Object>();
            requestBodyArguments.put(LoggerConstants.BODY, body);
            logger.log(level, "Request Body {}", requestBodyArguments);
        }
    }

    /**
     * Log Responses.
     *
     * @param response HttpResponse to be logged.
     */
    public void logResponse(Response response) {
        Level level = config.getLevel() != null ? config.getLevel() : Level.INFO;

        String contentLength = response.getHeaders().value(LoggerConstants.CONTENT_LENGTH);
        String contentType = response.getHeaders().value(LoggerConstants.CONTENT_TYPE);

        Map<String, Object> responseArguments = new LinkedHashMap<String, Object>();
        responseArguments.put(LoggerConstants.STATUS_CODE, response.getStatusCode());
        responseArguments.put(LoggerConstants.CONTENT_TYPE, contentType);
        responseArguments.put(LoggerConstants.CONTENT_LENGTH, contentLength);
        logger.log(level, "Response {} {} content-length: {}", responseArguments);

        if (responseLogOptions.shouldLogHeaders()) {
            Map<String, String> headersToLog = LoggerUtilities.getHeadersToLog(responseLogOptions,
                    response.getHeaders().asSimpleMap(), config.getMaskSensitiveHeaders());

            Map<String, Object> responseHeaderArguments = new LinkedHashMap<String, Object>();
            responseHeaderArguments.put(LoggerConstants.HEADERS, headersToLog);
            logger.log(level, "Response Headers {}", responseHeaderArguments);
        }

        if (responseLogOptions.shouldLogBody()) {
            Map<String, Object> responseBodyArguments = new LinkedHashMap<String, Object>();
            responseBodyArguments.put(LoggerConstants.BODY, response.getBody());
            logger.log(level, "Response Body {}", responseBodyArguments);
        }
    }
}
