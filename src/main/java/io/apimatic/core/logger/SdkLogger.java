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
	 * Default Constructor.
	 * 
	 * @param config {@link ReadonlyLoggingConfiguration} as logging properties.
	 */
	public SdkLogger(final ReadonlyLoggingConfiguration config) {
		this.config = config;
		this.logger = config.getLogger();
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
		String contentType = request.getHeaders().value("content-type") != null
				? request.getHeaders().value("content-type")
				: "";

		Map<String, Object> requestArguments = new LinkedHashMap<String, Object>();
		requestArguments.put(LoggerConstants.METHOD, request.getHttpMethod());
		requestArguments.put(LoggerConstants.URL, url);
		requestArguments.put(LoggerConstants.CONTENT_TYPE, contentType);
		if (config.getRequestLogOptions().shouldIncludeQueryInPath()) {
			requestArguments.put(LoggerConstants.QUERY_PARAMETER, queryParameter);
			logger.log(level, "Request {} {} {} queryParameters: {}", requestArguments);
		} else {
			logger.log(level, "Request {} {} {}", requestArguments);
		}

		if (config.getRequestLogOptions().shouldLogHeaders()) {
			Map<String, String> extractedHeaders = LoggerUtilities.extractHeadersToLog(
					request.getHeaders().asSimpleMap(), config.getRequestLogOptions().getHeadersToInclude(),
					config.getRequestLogOptions().getHeadersToExclude());

			Map<String, String> headersToLog = LoggerUtilities.filterSensitiveHeaders(extractedHeaders,
					config.getMaskSensitiveHeaders());

			Map<String, Object> requestHeaderArguments = new LinkedHashMap<String, Object>();
			requestHeaderArguments.put(LoggerConstants.HEADERS, headersToLog);
			logger.log(level, "Request Headers {}", requestHeaderArguments);
		}

		if (config.getRequestLogOptions().shouldLogBody()) {
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

		String contentLength = response.getHeaders().value("content-length");
		String contentType = response.getHeaders().value("content-type");

		Map<String, Object> responseArguments = new LinkedHashMap<String, Object>();
		responseArguments.put(LoggerConstants.STATUS_CODE, response.getStatusCode());
		responseArguments.put(LoggerConstants.CONTENT_TYPE, contentType);
		responseArguments.put(LoggerConstants.CONTENT_LENGTH, contentLength);
		logger.log(level, "Response {} {} content-length: {}", responseArguments);

		if (config.getResponseLogOptions().shouldLogHeaders()) {
			Map<String, String> extractedHeaders = LoggerUtilities.extractHeadersToLog(
					response.getHeaders().asSimpleMap(), config.getResponseLogOptions().getHeadersToInclude(),
					config.getResponseLogOptions().getHeadersToExclude());

			Map<String, String> headersToLog = LoggerUtilities.filterSensitiveHeaders(extractedHeaders,
					config.getMaskSensitiveHeaders());

			Map<String, Object> responseHeaderArguments = new LinkedHashMap<String, Object>();
			responseHeaderArguments.put(LoggerConstants.HEADERS, headersToLog);
			logger.log(level, "Response Headers {}", responseHeaderArguments);
		}

		if (config.getResponseLogOptions().shouldLogBody()) {
			Map<String, Object> responseBodyArguments = new LinkedHashMap<String, Object>();
			responseBodyArguments.put(LoggerConstants.BODY, response.getBody());
			logger.log(level, "Response Body {}", responseBodyArguments);
		}
	}
}
